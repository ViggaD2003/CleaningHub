package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "voucher")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Duration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "area")
    private Double area;

    @Column(name = "number_of_worker")
    private Integer numberOfWork;

    @Column(name = "duration_in_hours")
    private Integer durationInHours;

    @OneToOne(mappedBy = "duration")
    private Booking booking;
}
