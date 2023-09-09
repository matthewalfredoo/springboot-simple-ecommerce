package com.learning.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {

    private boolean success;
    private String message;
    private Long timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL) // this will exclude the field if it is null
    private Object data = null;

    @JsonInclude(JsonInclude.Include.NON_NULL) // this will exclude the field if it is null
    private Object error = null;

}
