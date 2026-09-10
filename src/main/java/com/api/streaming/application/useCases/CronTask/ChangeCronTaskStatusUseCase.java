package com.api.streaming.application.useCases.CronTask;

import com.api.streaming.application.interfaces.CronTaskInterface;
import com.api.streaming.application.queries.CronTaskQuery;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class ChangeCronTaskStatusUseCase {
    private final CronTaskInterface cronTaskInterface;

    public void execute(String id, boolean status) {
        CronTaskQuery query = this.verifyStatus(id, status);

        if (query.active() == status)
            throw new IllegalArgumentException("Cannot update status if you are sending same status");
        
        this.cronTaskInterface.save(
            new CronTaskQuery(query.id(), query.name(), query.description(), status)
        );
    }

    private CronTaskQuery verifyStatus(String id, boolean newStatus) {
        return this.cronTaskInterface.getTask(id)
            .orElseThrow(() -> new EntityNotFoundException("None task found with this id"));
    }
}
