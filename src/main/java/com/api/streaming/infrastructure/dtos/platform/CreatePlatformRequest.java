package com.api.streaming.infrastructure.dtos.platform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlatformRequest {
    private String name;
    private String link;
    private String icon;
}
