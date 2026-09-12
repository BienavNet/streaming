package com.api.streaming.application.useCases.CronTask;

import com.api.streaming.application.interfaces.CronTaskInterface;
import com.api.streaming.application.queries.CronTaskQuery;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class CheckIfCanExecuteCronTaskUseCase {
    private final CronTaskInterface cronTaskInterface;

    public void execute(String id) {
        CronTaskQuery task = this.cronTaskInterface.getTask(id)
            .orElseThrow(() -> new EntityNotFoundException("Error, task not found"));

        if (!task.active())
            throw new IllegalArgumentException("Cannot execute this task because is disabled.");
    }
}
