package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CategoryRequest;
import com.fpu.exe.cleaninghub.dto.request.DurationRequest;
import com.fpu.exe.cleaninghub.dto.request.UpdateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.response.CreateServiceResponseDto;
import com.fpu.exe.cleaninghub.entity.Duration;
import com.fpu.exe.cleaninghub.services.interfc.DurationService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/durations")
@RequiredArgsConstructor
public class DurationController {
    private final DurationService durationService;
    @PostMapping("create")
    public ResponseEntity<?> createDuration(@RequestBody DurationRequest createDurationRequest){
        try{
            durationService.createDuration(createDurationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(API.Response.success("Create Duration successfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateDuration(@PathVariable Integer id,
                                           @Valid @RequestBody DurationRequest updateDurationRequest) {
        try {
            durationService.updateDuration(id, updateDurationRequest);
            return ResponseEntity.ok(API.Response.success("Update duration successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<Duration>> getDurationById(@PathVariable("id") int id) {
            List<Duration> durationList = durationService.getDurationsByServiceId(id);
            if (durationList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(durationList);
    }

}
