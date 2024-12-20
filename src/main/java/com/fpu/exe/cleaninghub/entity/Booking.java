package com.fpu.exe.cleaninghub.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpu.exe.cleaninghub.common.Auditable;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_detail_id", referencedColumnName = "id")
    private BookingDetail bookingDetail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address")
    private String address;

    // Thay đổi từ ManyToOne thành ManyToMany
    @ManyToMany
    @JoinTable(
            name = "booking_staff",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<User> staff;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @ManyToOne
    @JoinColumn(name = "duration_id")
    private Duration duration;

    @OneToOne(mappedBy = "booking")
    private Rating rating;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endDate;
}
