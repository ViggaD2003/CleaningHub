package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@Table(name = "voucher")
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "percentage")
    private Integer percentage;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @Column(name = "amount")
    private Integer amount;

    @OneToOne(mappedBy = "voucher")
    private BookingDetail bookingDetail;
}
