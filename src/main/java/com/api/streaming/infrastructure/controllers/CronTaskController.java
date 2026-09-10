package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.queries.CronTaskQuery;
import com.api.streaming.application.useCases.CronTask.ChangeCronTaskStatusUseCase;
import com.api.streaming.application.useCases.CronTask.GetAllCronTasksUseCase;
import com.api.streaming.infrastructure.dtos.GenericResponse;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;



@RestController 
@RequestMapping("/api/crontasks")
@AllArgsConstructor 
public class CronTaskController {
    private final ChangeCronTaskStatusUseCase changeCronTaskStatusUseCase;
    private  final GetAllCronTasksUseCase getAllCronTasksUseCase;


    @PutMapping("/status/{id}/{status}")
    public ResponseEntity<GenericResponse<Void>> updateStatus(@PathVariable String id, @PathVariable boolean status) {
        this.changeCronTaskStatusUseCase.execute(id, status);

        GenericResponse<Void> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Status Updated Successfully.");

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/")
    public ResponseEntity<GenericResponse<List<CronTaskQuery>>> getTasks() {

        GenericResponse<List<CronTaskQuery>> response = new GenericResponse<>();
        response.setData(this.getAllCronTasksUseCase.execute());
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
}
