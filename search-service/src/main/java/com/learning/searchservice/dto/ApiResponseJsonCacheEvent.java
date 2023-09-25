package com.learning.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseJsonCacheEvent {

    public static final String EVENT_TYPE_CREATE = "CREATE";

    private String eventId;
    private String eventType;

    // API URL
    private String key;

    // JSON
    private String value;

}
