package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.CreateBookingRequest;
import com.fpu.exe.cleaninghub.dto.response.*;
import com.fpu.exe.cleaninghub.entity.*;
import com.fpu.exe.cleaninghub.repository.TokenRepository;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.services.interfc.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class BookingServiceImpl implements BookingService {
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
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId).orElseThrow(() -> new IllegalArgumentException("Booking not found or you do not have access to this booking"));
        BookingDetailResponseDto dto = new BookingDetailResponseDto();
        dto.setId(booking.getId());
        dto.setCreateDate(booking.getBookingDate());
        dto.setUpdateDate(booking.getUpdateDate());
        if(booking.getBookingDetails() != null && !booking.getBookingDetails().isEmpty()){
            BookingDetail bookingDetail = booking.getBookingDetails().get(0);
            Voucher voucher = bookingDetail.getVoucher();
            if (voucher != null){
                VoucherResponseDto voucherDto = new VoucherResponseDto();
                voucherDto.setId(voucher.getId());
                voucherDto.setAmount(voucher.getAmount());
                voucherDto.setPercentage(voucher.getPercentage());
                voucherDto.setCreateDate(voucher.getCreateDate());
                voucherDto.setUpdateDate(voucher.getUpdateDate());
                voucherDto.setExpiredDate(voucher.getExpiredDate());
                dto.setVoucher(voucherDto);
            }
            List<PaymentResponseDto> paymentDtos = bookingDetail.getPayments().stream().map(payments ->{
                PaymentResponseDto paymentDto = new PaymentResponseDto();
                paymentDto.setId(payments.getId());
                paymentDto.setCreateDate(payments.getCreateDate());
                paymentDto.setFinalPrice(payments.getFinalPrice());
                return paymentDto;
            }).toList();
            dto.setPayments(paymentDtos);
        }
        return dto;
    }

    private BookingResponseDto convertToDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setBookingDate(booking.getBookingDate());
        dto.setAddress(booking.getAddress());
        dto.setServiceName(booking.getService().getName());
        dto.setStaffName(booking.getStaff() != null ? booking.getStaff().getFullName() : null);
        return dto;
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
    @Override
    public CreateBookingResponse createBooking(CreateBookingRequest createBookingRequest) {
        // Fetch the selected service and duration
        com.fpu.exe.cleaninghub.entity.Service serviceSelected = serviceRepository
                .findById(createBookingRequest.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Duration durationSelected = durationRepository
                .findById(createBookingRequest.getDurationId())
                .orElseThrow(() -> new RuntimeException("Duration not found"));

        // Handle voucher if provided
        Voucher voucherSelected = null;
        if (createBookingRequest.getVoucherId() != null) {
            voucherSelected = voucherRepository
                    .findById(createBookingRequest.getVoucherId())
                    .orElseThrow(() -> new RuntimeException("Voucher not found"));
        }

        // Calculate the final price based on service and duration
        double finalPrice = calculateFinalPrice(serviceSelected, durationSelected, voucherSelected);

        // Create payment details
        Payments payment = Payments.builder()
                .finalPrice(finalPrice)
                .createDate(LocalDate.now())
                .build();

        paymentRepository.save(payment);

        // Create booking detail
        BookingDetail bookingDetail = BookingDetail.builder()
                .createDate(LocalDate.now())
                .updateDate(LocalDate.now())
                .voucher(voucherSelected)
                .payment(payment)
                .build();

        bookingDetailRepository.save(bookingDetail);

        // Fetch staff and user
        User staff = userRepository.findStaffByHighestAverageRating();
        User user = userRepository.findByEmail(createBookingRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and save the booking
        Booking booking = Booking.builder()
                .bookingDate(LocalDate.now())
                .updateDate(LocalDate.now())
                .bookingDetail(bookingDetail)
                .service(serviceSelected)
                .staff(staff)
                .user(user)
                .duration(durationSelected)
                .address(createBookingRequest.getAddress())
                .build();

        bookingRepository.save(booking);

        // Create and return response

        return CreateBookingResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .bookingDate(booking.getBookingDate())
                .updateDate(booking.getUpdateDate())
                .bookingDetail(modelMapper.map(bookingDetail, BookingDetailResponse.class))
                .service(modelMapper.map(serviceSelected, ServiceResponseDto.class))
                .staff(modelMapper.map(staff, UserResponseDTO.class))
                .user(modelMapper.map(user, UserResponseDTO.class))
                .duration(modelMapper.map(durationSelected, DurationResponse.class))
                .address(createBookingRequest.getAddress())
                .build();
    }

    private double calculateFinalPrice(com.fpu.exe.cleaninghub.entity.Service service, Duration duration, Voucher voucher) {
        double basePrice = service.getBasePrice();
        // Example calculation, add your logic here
        double finalPrice = basePrice;  // You should replace this with actual price calculation logic

        if (voucher != null) {
            finalPrice -= voucher.getAmount();  // Example discount application
        }

        return finalPrice;
    }
}
