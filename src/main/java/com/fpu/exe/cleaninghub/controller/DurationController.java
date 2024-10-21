package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.entity.Duration;
import com.fpu.exe.cleaninghub.services.interfc.DurationService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/durations")
@RequiredArgsConstructor
public class DurationController {
    private final DurationService durationService;
    @GetMapping("/getAll")
    public ResponseEntity<List<Duration>> getAllDurations() {
            List<Duration> durationList = durationService.getAllDurations();
            if (durationList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(durationList);
    }
}
