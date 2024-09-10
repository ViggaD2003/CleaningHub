package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {
    private Integer id;
    private String name;
    private String description;
    public static CategoryResponseDTO fromCategory(Category category){
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
