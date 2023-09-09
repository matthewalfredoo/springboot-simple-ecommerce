package com.learning.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto extends ApiResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL) // this will exclude the field if it is null
    private String token;

}
