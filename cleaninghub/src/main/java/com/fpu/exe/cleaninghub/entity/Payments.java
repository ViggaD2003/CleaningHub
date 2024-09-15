package com.fpu.exe.cleaninghub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpu.exe.cleaninghub.common.Auditable;
import com.fpu.exe.cleaninghub.enums.Payment.PaymentMethod;
import com.fpu.exe.cleaninghub.enums.Payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payment")
public class Payments extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "final_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;  // Use the updated PaymentStatus enum

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;  // Use the updated PaymentMethod enum

    @Column(name = "transaction_id", length = 50, unique = true)
    private String transactionId;  // Optional: only for non-cash payments

    @JsonIgnore
    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private BookingDetail bookingDetail;
}
