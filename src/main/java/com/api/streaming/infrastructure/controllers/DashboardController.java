package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.queries.dashboard.DashboardInfoQuery;
import com.api.streaming.application.useCases.Dashboard.GetDashboardDetailsUseCase;
import com.api.streaming.infrastructure.dtos.GenericResponse;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final GetDashboardDetailsUseCase getDashboardDetailsUseCase;

    @GetMapping("")
    public ResponseEntity<GenericResponse<DashboardInfoQuery>> getDashboardDetails() {
        
        DashboardInfoQuery info = this.getDashboardDetailsUseCase.execute();

        GenericResponse<DashboardInfoQuery> response = new GenericResponse<>();
        response.setData(info);
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
}
