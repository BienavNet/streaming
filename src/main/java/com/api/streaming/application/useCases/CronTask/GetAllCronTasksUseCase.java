package com.api.streaming.application.useCases.CronTask;

import java.util.List;

import com.api.streaming.application.interfaces.CronTaskInterface;
import com.api.streaming.application.queries.CronTaskQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class GetAllCronTasksUseCase {
    private final CronTaskInterface cronTaskInterface;

    public List<CronTaskQuery> execute() {
        return  this.cronTaskInterface.getTasks();
    }
}
