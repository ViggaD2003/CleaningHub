package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.Booking;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Builder
@Data
public class RatingDTO {
    private Long id;

    private Integer stars;

    private LocalDate ratingDate;

    private String comments;
}
