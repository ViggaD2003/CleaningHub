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
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;


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
        dto.setCreatedDate(booking.getCreatedDate());
        dto.setUpdatedDate(booking.getUpdatedDate());
        if(booking.getBookingDetail() != null){
            BookingDetail bookingDetail = booking.getBookingDetail();
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
            PaymentResponseDto paymentDto = modelMapper.map(bookingDetail.getPayment(), PaymentResponseDto.class);
            dto.setPayment(paymentDto);
        }
        return dto;
    }

    private BookingResponseDto convertToDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setBookingDate(booking.getCreatedDate());
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO createBookingRequestDTO) {
        // Fetch the selected service and duration
        com.fpu.exe.cleaninghub.entity.Service serviceSelected = serviceRepository
                .findById(createBookingRequestDTO.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Duration durationSelected = durationRepository
                .findByIdAndServiceId(createBookingRequestDTO.getDurationId(), serviceSelected.getId())
                .orElseThrow(() -> new RuntimeException("Duration not found"));

        // Handle voucher if provided
        Voucher voucherSelected = null;
        if (createBookingRequestDTO.getVoucherId() != null) {
            voucherSelected = voucherRepository
                    .findById(createBookingRequestDTO.getVoucherId())
                    .orElseThrow(() -> new RuntimeException("Voucher not found"));
        }

        // Calculate the final price based on service, duration, and voucher
        BigDecimal finalPrice = calculateFinalPrice(serviceSelected, durationSelected, voucherSelected);

        // Determine payment status based on payment method
        PaymentStatus paymentStatus;
        String transactionId = null; // For online payments, we'll set this later

        if (createBookingRequestDTO.getPaymentMethod() == PaymentMethod.CASH) {
            // Cash payments are completed after the service is done
            paymentStatus = PaymentStatus.PENDING; // Pending until the service is completed
        } else {
            // For online banking (e.g., MOMO, VNPAY, PAYOS), we might set it to pending until the bank confirms the payment
            paymentStatus = PaymentStatus.PENDING;
            // Optionally, generate or store transaction ID if your system works with external payment gateways
//            transactionId = generateTransactionId(createBookingRequestDTO.getPaymentMethod());
        }

        // Create payment details
        Payments payment = Payments.builder()
                .finalPrice(finalPrice)
                .paymentMethod(createBookingRequestDTO.getPaymentMethod())
                .paymentStatus(paymentStatus)
                .transactionId(transactionId) // Only relevant for online payments
                .build();

        paymentRepository.save(payment);

        // Create booking detail
        BookingDetail bookingDetail = BookingDetail.builder()
                .voucher(voucherSelected)
                .payment(payment)
                .build();

        bookingDetailRepository.save(bookingDetail);

        // Fetch staff and user
        User staff = userRepository.findStaffByHighestAverageRating();
        User user = userRepository.findByEmail(createBookingRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and save the booking
        Address address = Address.builder()
                .street(createBookingRequestDTO.getAddress().getStreet())
                .city(createBookingRequestDTO.getAddress().getCity())
                .state(createBookingRequestDTO.getAddress().getState())
                .zipCode(createBookingRequestDTO.getAddress().getZipCode())
                .country(createBookingRequestDTO.getAddress().getCountry())
                .build();

        Booking booking = Booking.builder()
                .bookingDetail(bookingDetail)
                .service(serviceSelected)
                .staff(staff)
                .user(user)
                .duration(durationSelected)
                .address(address)
                .status(BookingStatus.AWAITING_CONFIRMATION)
                .build();

        bookingRepository.save(booking);

        // Create and return the final response
        return CreateBookingResponseDTO.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .address(AddressResponseDTO.builder()
                        .street(address.getStreet())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipCode(address.getZipCode())
                        .country(address.getCountry())
                        .build())
                .bookingDetail(modelMapper.map(bookingDetail, BookingDetailResponseDto.class))
                .service(modelMapper.map(serviceSelected, ServiceDetailResponseDTO.class))
                .staff(modelMapper.map(staff, UserResponseDTO.class))
                .user(modelMapper.map(user, UserResponseDTO.class))
                .duration(modelMapper.map(durationSelected, DurationResponseDTO.class))
                .createdDate(booking.getCreatedDate())
                .updatedDate(booking.getUpdatedDate())
                .build();
    }

    private BigDecimal calculateFinalPrice(com.fpu.exe.cleaninghub.entity.Service service, Duration duration, Voucher voucher) {
        // Convert the base price to BigDecimal
        BigDecimal basePrice = BigDecimal.valueOf(service.getBasePrice());

        // Initialize finalPrice with basePrice
        BigDecimal finalPrice = basePrice;

        // Apply voucher discount if applicable
        if (voucher != null) {
            BigDecimal voucherAmount = BigDecimal.valueOf(voucher.getAmount());
            finalPrice = finalPrice.subtract(voucherAmount);  // Subtract the voucher amount
        }

        // Ensure the final price is non-negative and round to 2 decimal places
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }

        // Round the final price to 2 decimal places (for currency)
        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
