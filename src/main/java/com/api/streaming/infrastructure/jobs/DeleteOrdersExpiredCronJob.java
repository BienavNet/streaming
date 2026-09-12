package com.api.streaming.infrastructure.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.streaming.application.useCases.CronTask.CheckIfCanExecuteCronTaskUseCase;
import com.api.streaming.application.useCases.Order.DeleteExpiredOrdersUseCase;

import lombok.RequiredArgsConstructor;

@Component(DeleteOrdersExpiredCronJob.TASK_ID)
@RequiredArgsConstructor 
public class DeleteOrdersExpiredCronJob {

    public static final String TASK_ID = "delete_expired_orders_crontask";

    private final DeleteExpiredOrdersUseCase deleteExpiredOrdersUseCase;
    private final CheckIfCanExecuteCronTaskUseCase checkIfCanExecuteCronTaskUseCase;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(DeleteOrdersExpiredCronJob.class);

    @Scheduled(cron = "0 0 0 * * *")
    public void executeJob() {
        logger.info("[starting] CronJob: Delete expired orders (DeleteOrdersExpiredCronJob.class)");

        try {
            this.checkIfCanExecuteCronTaskUseCase.execute(TASK_ID);
        } catch (Exception e) {
            logger.warn("[cancelled] CronJob skipped: {}", e.getMessage());
            return;
        }

        this.deleteExpiredOrdersUseCase.execute();
        logger.info("[finished] CronJob: Delete expired orders successfully.");
    }
}
