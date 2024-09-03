package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

        @ManyToOne
        @JoinColumn(name = "staff_id")
        private User staff;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @Column(name = "stars")
        private Integer stars;

        @Column(name = "rating_date")
        private LocalDate ratingDate;

        @Column(name = "comments")
        private String comments;

}


