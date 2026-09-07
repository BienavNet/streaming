package com.api.streaming.application.useCases.Platform;

import java.util.List;
import java.util.Map;

import com.api.streaming.application.interfaces.strategies.PlatformFilterStrategy;
import com.api.streaming.domain.models.Platform;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilterPlatformUseCase {
    
    private final Map<String, PlatformFilterStrategy> strategies;

    public List<Platform> execute(String filterKey, String criteria) {
        PlatformFilterStrategy strategy = this.strategies.get(filterKey);

        if (strategy == null)
            throw new IllegalArgumentException("This filter key does not exist.");

        return strategy.filter(criteria);
    }
}
