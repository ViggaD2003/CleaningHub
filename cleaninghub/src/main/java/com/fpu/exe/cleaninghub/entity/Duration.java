package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Table(name = "duration")
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

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "duration")
    private Set<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
}
