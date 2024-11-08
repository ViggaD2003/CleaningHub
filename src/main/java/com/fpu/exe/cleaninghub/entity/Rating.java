package com.fpu.exe.cleaninghub.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rating")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating
{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "booking_id", referencedColumnName = "id")
        private Booking booking;

        @Column(name = "stars")
        private Integer stars;

        @Column(name = "rating_date",  nullable = false, updatable = false)
        private LocalDate ratingDate;

        @Column(name = "comments")
        private String comments;

        @PrePersist
        void onCreated () {
                ratingDate = LocalDate.now();
        }

}


