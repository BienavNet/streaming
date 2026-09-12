package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.CronTaskInterface;
import com.api.streaming.application.useCases.CronTask.ChangeCronTaskStatusUseCase;
import com.api.streaming.application.useCases.CronTask.CheckIfCanExecuteCronTaskUseCase;
import com.api.streaming.application.useCases.CronTask.GetAllCronTasksUseCase;

@Configuration 
public class CronTaskUseCasesConfig {

    @Bean 
    ChangeCronTaskStatusUseCase changeCronTaskStatusUseCase(
        CronTaskInterface cronTaskInterface
    ){
        return new ChangeCronTaskStatusUseCase(cronTaskInterface);
    }

    @Bean 
    GetAllCronTasksUseCase getAllCronTasksUseCase(
        CronTaskInterface cronTaskInterface
    ) {
        return new GetAllCronTasksUseCase(cronTaskInterface);
    }

    @Bean 
    CheckIfCanExecuteCronTaskUseCase checkIfCanExecuteCronTaskUseCase(
        CronTaskInterface cronTaskInterface
    ) {
        return checkIfCanExecuteCronTaskUseCase(cronTaskInterface);
    }
    
}
