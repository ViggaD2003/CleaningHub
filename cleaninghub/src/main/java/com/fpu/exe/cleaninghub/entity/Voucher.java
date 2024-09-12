package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@Table(name = "voucher")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "percentage")
    private Integer percentage;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDate createDate;

    @Column(name = "update_date", insertable = false)
    @LastModifiedDate
    private LocalDate updateDate;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @Column(name = "amount")
    private Integer amount;

    @OneToOne(mappedBy = "voucher")
    private BookingDetail bookingDetail;
}
