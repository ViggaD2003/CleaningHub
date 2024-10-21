package com.fpu.exe.cleaninghub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateServiceRequestDto {

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be a positive number")
    private Double basePrice;

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @Pattern(regexp = "^(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)$",
            message = "Image URL must be a valid URL ending with .jpg, .gif, or .png")
    private String img;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;
}
