package com.fpu.exe.cleaninghub.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryRequest {
    private String name;

    private String description;
}
