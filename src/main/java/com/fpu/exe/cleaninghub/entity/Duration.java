package com.fpu.exe.cleaninghub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "duration_in_hours")
    private Integer durationInHours;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "duration")
    @JsonIgnore
    private Set<Booking> bookings;
}
