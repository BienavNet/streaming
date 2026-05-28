package com.api.streaming.domain.models;

import com.api.streaming.domain.valueObjects.PlatformId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Platform {
    private PlatformId id;
    private String name;
    private String link;
    private String icon;
}
