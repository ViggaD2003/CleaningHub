package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "rating")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

        @CreatedDate
        @Column(name = "rating_date",  nullable = false, updatable = false)
        private LocalDate ratingDate;

        @Column(name = "comments")
        private String comments;

}


