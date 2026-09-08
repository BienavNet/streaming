package com.api.streaming.infrastructure.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.streaming.application.useCases.Order.DeleteExpiredOrdersUseCase;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class DeleteOrdersExpiredCronJob {

    private final DeleteExpiredOrdersUseCase deleteExpiredOrdersUseCase;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(DeleteOrdersExpiredCronJob.class);

    @Scheduled(cron = "0 0 0 * * *")
    public void executeJob() {
        logger.info("[starting] CronJob: Delete expired orders (DeleteOrdersExpiredCronJob.class)");
        
        this.deleteExpiredOrdersUseCase.execute();
    }
}
