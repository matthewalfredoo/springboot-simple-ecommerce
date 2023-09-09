package com.learning.productservice.exception;

import com.learning.productservice.dto.ApiResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponseDto> handleAllExceptions(Exception ex, WebRequest webRequest) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(false);
        apiResponseDto.setMessage("Something went wrong");
        apiResponseDto.setPath(webRequest.getDescription(false));
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setError(ex.getClass().getSimpleName() + ": " + ex.getMessage());

        return new ResponseEntity<>(apiResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ApiResponseDto> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest webRequest
    ) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(false);
        apiResponseDto.setMessage("Product not found");
        apiResponseDto.setPath(webRequest.getDescription(false));
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setError(exception.getClass().getSimpleName() + ": " + exception.getMessage());

        return new ResponseEntity<>(apiResponseDto, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Total errors: " + ex.getErrorCount() + " First Error: " + ex.getFieldError(),
                request.getDescription(false),
                "BAD_REQUEST"
        );

        return new ResponseEntity<Object>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
