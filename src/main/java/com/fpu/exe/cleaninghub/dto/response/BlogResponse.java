package com.fpu.exe.cleaninghub.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    Integer id;
    String title;
    String content;
    String img;
}
