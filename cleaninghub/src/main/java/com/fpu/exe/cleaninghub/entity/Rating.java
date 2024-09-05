package com.fpu.exe.cleaninghub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

        @CreatedDate
        @Column(name = "rating_date",  nullable = false, updatable = false)
        private LocalDate ratingDate;

        @Column(name = "comments")
        private String comments;

}


