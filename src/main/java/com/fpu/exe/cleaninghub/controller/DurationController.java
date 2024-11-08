package com.fpu.exe.cleaninghub.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpu.exe.cleaninghub.entity.Duration;
import com.fpu.exe.cleaninghub.services.interfc.DurationService;

import lombok.RequiredArgsConstructor;

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
