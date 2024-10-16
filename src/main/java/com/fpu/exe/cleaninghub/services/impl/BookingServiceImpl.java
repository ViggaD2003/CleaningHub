package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.CreateBookingRequestDTO;
import com.fpu.exe.cleaninghub.dto.response.*;
import com.fpu.exe.cleaninghub.entity.*;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import com.fpu.exe.cleaninghub.enums.Payment.PaymentMethod;
import com.fpu.exe.cleaninghub.enums.Payment.PaymentStatus;
import com.fpu.exe.cleaninghub.repository.*;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.services.interfc.JWTService;
import com.fpu.exe.cleaninghub.services.interfc.RatingService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private DurationRepository durationRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RatingService ratingService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Page<BookingResponseDto> searchBookings(HttpServletRequest request, String searchTerm, int pageIndex, int pageSize) {
        User currentUser = getCurrentUser(request);
        Integer userId = currentUser.getId();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Booking> bookingsPage = bookingRepository.findByUserId(userId, searchTerm, pageable);
        return bookingsPage.map(this::convertToDto);
    }

    @Override
    public BookingDetailResponseDto getBookingDetail(HttpServletRequest request, Integer bookingId) {
        User currentUser = getCurrentUser(request);
        Integer userId = currentUser.getId();

        // Retrieve booking and validate ownership
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or you do not have access to this booking"));

        return mapToBookingDetailResponseDto(booking);
    }

    public User getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token == null) {
            throw new IllegalArgumentException("No JWT token found in the request header");
        }
        final var accessToken = tokenRepository.findByToken(token).orElse(null);
        if (accessToken == null) {
            throw new IllegalArgumentException("No JWT token is valid!");
        }
        String email = jwtService.extractUsername(token);
        var user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not Found User"));
        if (!jwtService.isTokenValid(token, user) || accessToken.isRevoked() || accessToken.isExpired()) {
            throw new IllegalArgumentException("JWT token has expired or been revoked");
        }
        return user;
    }
    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO createBookingRequestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));



        Duration durationSelected = durationRepository
                .findById(createBookingRequestDTO.getDurationId()).get();

        com.fpu.exe.cleaninghub.entity.Service service = serviceRepository.findById(createBookingRequestDTO.getServiceId()).get();

        LocalDateTime endTime = createBookingRequestDTO.getStartTime().plusHours(durationSelected.getDurationInHours());
        List<User> availableStaffs = userRepository.findStaffByBookingTime(createBookingRequestDTO.getStartTime(), endTime);

        if(availableStaffs.isEmpty()){
            throw new RuntimeException("The staffs are busy at this time. Please choose another time");
        }
        List<User> listStaff = findAvailableStaff(availableStaffs, createBookingRequestDTO.getNumberOfWorker());


        // Handle voucher if provided
        Voucher voucherSelected = null;
        if (createBookingRequestDTO.getVoucherId() != null) {
            voucherSelected = voucherRepository
                    .findById(createBookingRequestDTO.getVoucherId())
                    .orElseThrow(() -> new RuntimeException("Voucher not found"));
        }

        // Calculate the final price based on service, duration, and voucher
        BigDecimal finalPrice = calculateFinalPrice(service, durationSelected, voucherSelected, createBookingRequestDTO.getNumberOfWorker());

        // Determine payment status based on payment method
        PaymentStatus paymentStatus = null;

        if (createBookingRequestDTO.getPaymentMethod() == PaymentMethod.CASH || createBookingRequestDTO.getPaymentMethod() == PaymentMethod.PAYOS) {
            // Cash payments are completed after the service is done
            paymentStatus = PaymentStatus.PENDING; // Pending until the service is completed
        }

        // Create payment details
        Payments payment = Payments.builder()
                .finalPrice(finalPrice)
                .paymentMethod(createBookingRequestDTO.getPaymentMethod())
                .paymentStatus(paymentStatus)
                .build();

        paymentRepository.save(payment);

        // Create booking detail
        BookingDetail bookingDetail = BookingDetail.builder()
                .voucher(voucherSelected)
                .payment(payment)
                .build();

        bookingDetailRepository.save(bookingDetail);

        Booking booking = Booking.builder()
                .bookingDetail(bookingDetail)
                .service(service)
                .staff(listStaff)
                .user(user)
                .duration(durationSelected)
                .address(createBookingRequestDTO.getAddress())
                .status(BookingStatus.PENDING)
                .startDate(createBookingRequestDTO.getStartTime())
                .endDate(endTime)
                .build();

        bookingRepository.save(booking);
//        try {
//            simpMessagingTemplate.convertAndSendToUser(staff.getEmail(),"/queue/notifications", booking);
//            log.info("Websocket notification sent to staff successfully for booking id: {}", staff.getEmail());
//        } catch (Exception e){
//            log.error("Failed to send Websocket notification to staff for booking id: {}", booking.getId());
//            throw new RuntimeException("Failed to send Websocket notification");
//        }

        // Create and return the final response
        return CreateBookingResponseDTO.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .address(booking.getAddress())
                .bookingDetail(modelMapper.map(bookingDetail, BookingDetailResponseDto.class))
                .service(modelMapper.map(service, ServiceDetailResponseDTO.class))
                .staff(listStaff.stream().map(staff -> modelMapper.map(staff, UserResponseDTO.class)).toList())
                .user(modelMapper.map(user, UserResponseDTO.class))
                .duration(modelMapper.map(durationSelected, DurationResponseDTO.class))
                .createdDate(booking.getCreatedDate())
                .updatedDate(booking.getUpdatedDate())
                .startedAt(createBookingRequestDTO.getStartTime())
                .endAt(endTime)
                .build();
    }

    @Override
    public Page<ListBookingResponseDTO> getAllStaffBookings(HttpServletRequest request, BookingStatus bookingStatus, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findByStaffId(getCurrentUser(request).getId(),bookingStatus, pageable);
        Page<ListBookingResponseDTO> pageList = bookings.map(booking -> modelMapper.map(booking, ListBookingResponseDTO.class));
        for(ListBookingResponseDTO x : pageList){
            x.setCurrentStaff(getCurrentUser(request));
        }
        return pageList;
    }

    @Override
    public List<ListBookingResponseDTO> getAllStaffBookings(HttpServletRequest request) {
        List<Booking> bookings = bookingRepository.findByStaffIdWithStatusPending(getCurrentUser(request).getId());
        List<ListBookingResponseDTO> list = bookings.stream().map(booking -> modelMapper.map(booking, ListBookingResponseDTO.class)).toList();
        for(ListBookingResponseDTO x : list){
            x.setCurrentStaff(getCurrentUser(request));
        }
        return list;
    }


    @Override
    public void ChangeBookingStatus(BookingStatus bookingStatus, Integer id, HttpServletRequest request) {
        User currentUser = getCurrentUser(request);
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Payments payments = booking.getBookingDetail().getPayment();

        booking.getStaff().forEach(staff -> {
            if (!Objects.equals(currentUser.getId(), staff.getId()))
                throw new RuntimeException("You are not staff for this service!!!!");
        });

        booking.setStatus(
                switch (bookingStatus){
                    case PENDING -> BookingStatus.PENDING;
                    case CONFIRMED -> BookingStatus.CONFIRMED;
                    case IN_PROGRESS -> BookingStatus.IN_PROGRESS;
                    case COMPLETED -> BookingStatus.COMPLETED;
                    case CANCELLED -> BookingStatus.CANCELLED;
                    default -> booking.getStatus();
                }
        );
        if (booking.getStatus().equals(BookingStatus.COMPLETED)){
            if (payments.getPaymentMethod().equals(PaymentMethod.CASH)){
                payments.setPaymentStatus(PaymentStatus.SUCCESS);
                paymentRepository.save(payments);
            }
        }
        bookingRepository.save(booking);
    }

    @Override
    public List<User> findAvailableStaff(List<User> availableStaffs, Integer numberOfWorker) {
        availableStaffs.sort(Comparator.comparing(User::getAverageRating)
                .thenComparing(staff -> ratingService.numberOfRatings(staff.getId()))
                .reversed());

        List<User> staffs = availableStaffs.stream()
                .limit(numberOfWorker)  // Giới hạn theo numberOfWorker
                .collect(Collectors.toList());
        return staffs;
    }

    @Override
    public void changePaymentStatusOfBooking(Long orderCode, Integer bookingId, PaymentStatus paymentStatus) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Payments payments = booking.getBookingDetail().getPayment();
        payments.setPaymentStatus(
                switch (paymentStatus){
                    case SUCCESS -> PaymentStatus.SUCCESS;
                    case FAILED -> PaymentStatus.FAILED;
                    default -> payments.getPaymentStatus();
                }
        );

        booking.setStatus(
                switch (payments.getPaymentStatus()){
                    case SUCCESS -> BookingStatus.IN_PROGRESS;
                    case FAILED -> BookingStatus.CANCELLED;
                    default -> booking.getStatus();
                }
        );


        payments.setTransactionId(String.valueOf(orderCode));
        paymentRepository.save(payments);
        bookingRepository.save(booking);
    }

    private BigDecimal calculateFinalPrice(com.fpu.exe.cleaninghub.entity.Service service, Duration duration, Voucher voucher, Integer numberOfWorker) {
        Double basePrice = service.getBasePrice();
        double finalPrice = (double) 0;
        finalPrice += basePrice + ((duration.getPrice() * duration.getDurationInHours()) * numberOfWorker);
        if(voucher != null){
            finalPrice = finalPrice * voucher.getPercentage() / 100;
        }
        return BigDecimal.valueOf(finalPrice).setScale(3,RoundingMode.HALF_DOWN);
    }
    private BookingDetailResponseDto mapToBookingDetailResponseDto(Booking booking) {
        BookingDetailResponseDto dto = new BookingDetailResponseDto();
        dto.setId(booking.getId());

        BookingDetail bookingDetail = booking.getBookingDetail();
        if (bookingDetail != null) {
            dto.setVoucher(mapToVoucherResponseDto(bookingDetail.getVoucher()));
            dto.setPayment(mapToPaymentResponseDto(bookingDetail.getPayment()));
        }

        dto.setServiceName(booking.getService() != null ? booking.getService().getName() : null);
        dto.setStaffName(booking.getStaff() != null ? booking.getStaff().stream().map(staff ->  staff.getFullName()).toList() : null);
        dto.setAddress(booking.getAddress());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());

        return dto;
    }

    private VoucherResponseDto mapToVoucherResponseDto(Voucher voucher) {
        if (voucher == null) return null;

        VoucherResponseDto voucherDto = new VoucherResponseDto();
        voucherDto.setId(voucher.getId());
        voucherDto.setAmount(voucher.getAmount());
        voucherDto.setPercentage(voucher.getPercentage());
        voucherDto.setCreateDate(voucher.getCreateDate());
        voucherDto.setUpdateDate(voucher.getUpdateDate());
        voucherDto.setExpiredDate(voucher.getExpiredDate());
        return voucherDto;
    }

    private PaymentResponseDto mapToPaymentResponseDto(Payments payment) {
        return payment != null ? modelMapper.map(payment, PaymentResponseDto.class) : null;
    }

    private BookingResponseDto convertToDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setBookingDate(booking.getCreatedDate());
        dto.setAddress(booking.getAddress());
        dto.setServiceName(booking.getService().getName());
        dto.setStaffName(booking.getStaff() != null ? booking.getStaff().stream().map(staff ->  staff.getFullName()).toList() : null);
        return dto;
    }
}
