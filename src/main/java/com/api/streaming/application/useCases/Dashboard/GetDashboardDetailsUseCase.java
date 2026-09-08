package com.api.streaming.application.useCases.Dashboard;

import com.api.streaming.application.interfaces.DashboardInfoInterface;
import com.api.streaming.application.queries.dashboard.DashboardInfoQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetDashboardDetailsUseCase {
    private final DashboardInfoInterface dashboardInfoInterface;

    public DashboardInfoQuery execute() {
        return this.dashboardInfoInterface.getDetails();
    }
}
