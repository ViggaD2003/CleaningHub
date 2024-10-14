package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "systemSettings")
public class SystemSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "wait_duration")
    private Double waitDuration;

    @Column(name = "max_discount")
    private Double maxDiscount;

    @Column(name = "max_profit")
    private Double maxProfit;
}
