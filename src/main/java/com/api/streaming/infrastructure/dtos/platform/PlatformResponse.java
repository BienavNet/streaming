package com.api.streaming.infrastructure.dtos.platform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformResponse {
    private String id;
    private String name;
    private String link;
    private String icon;
}
