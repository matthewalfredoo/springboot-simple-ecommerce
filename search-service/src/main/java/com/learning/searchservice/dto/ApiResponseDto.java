package com.learning.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {

    private boolean success;
    private String message;
    private String timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL) // this will exclude the field if it is null
    private Object data = null;

    @JsonInclude(JsonInclude.Include.NON_NULL) // this will exclude the field if it is null
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL) // this will exclude the field if it is null
    private Object error = null;

}
