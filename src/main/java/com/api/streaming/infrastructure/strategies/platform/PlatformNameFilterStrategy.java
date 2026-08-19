package com.api.streaming.infrastructure.strategies.platform;

import java.util.List;

import org.springframework.stereotype.Component;

import com.api.streaming.application.interfaces.PlatformFilterStrategy;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;

import lombok.AllArgsConstructor;

@Component("platform_filter_name")
@AllArgsConstructor
public class PlatformNameFilterStrategy implements PlatformFilterStrategy{
    
    private final PlatformInterface platformInterface;
    
    @Override
    public List<Platform> filter(String criteria) {
        return this.platformInterface.findByName(criteria);
    }
    
}
