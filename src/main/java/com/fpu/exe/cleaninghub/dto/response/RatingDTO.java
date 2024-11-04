package com.fpu.exe.cleaninghub.dto.response;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class RatingDTO {
    private Long id;

    private Integer stars;

    private LocalDate ratingDate;

    private String comments;

    @Builder // Đặt @Builder tại constructor có tất cả các tham số
    public RatingDTO(Long id, Integer stars, LocalDate ratingDate, String comments) {
        this.id = id;
        this.stars = stars;
        this.ratingDate = ratingDate;
        this.comments = comments;
    }
}
