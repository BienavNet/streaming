package com.api.streaming.application.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.application.queries.CronTaskQuery;

public interface CronTaskInterface {
    List<CronTaskQuery> getTasks();
    Optional<CronTaskQuery> getTask(String id);
    void save(CronTaskQuery query);
}
