package com.fpu.exe.cleaninghub.config;

import com.fpu.exe.cleaninghub.dto.response.BookingDetailResponseDto;
import com.fpu.exe.cleaninghub.entity.BookingDetail;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Customize the mapping to ensure inherited fields are mapped
        modelMapper.addMappings(new PropertyMap<BookingDetail, BookingDetailResponseDto>() {
            @Override
            protected void configure() {
                map(source.getCreatedDate()).setCreatedDate(null);
                map(source.getUpdatedDate()).setUpdatedDate(null);
            }
        });

        return modelMapper;
    }
}
