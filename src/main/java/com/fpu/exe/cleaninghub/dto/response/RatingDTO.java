package com.fpu.exe.cleaninghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class RatingDTO {
    private Long id;

    private Integer stars;

    private LocalDate ratingDate;

    private String comments;
}
