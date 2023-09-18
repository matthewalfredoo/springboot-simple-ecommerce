package com.learning.authservice.controller;

import com.learning.authservice.dto.*;
import com.learning.authservice.entity.User;
import com.learning.authservice.mapper.UserMapper;
import com.learning.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> registerUser(
            @RequestBody
            AuthRequestDto authRequestDto
    ) {
        User user = UserMapper.toUserFromAuthRequestDto(authRequestDto);
        User savedUser = authService.saveUser(user);

        UserDto savedUserDto = UserMapper.toUserDtoFromUser(savedUser);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("User Registered Successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(savedUserDto);

        return ResponseEntity.ok(apiResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody
            AuthRequestDto authRequestDto
    ) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getEmail(), authRequestDto.getPassword()
                )
        );

        if(authenticate.isAuthenticated()) {
            User user = authService.getUserByEmail(authRequestDto.getEmail());

            String token = authService.generateToken(user.getId(), authRequestDto.getEmail(), user.getRole());

            UserDto userDto = UserMapper.toUserDtoFromUser(user);

            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setSuccess(true);
            loginResponseDto.setMessage("Login Successful");
            loginResponseDto.setTimestamp(LocalDateTime.now().toString());
            loginResponseDto.setData(userDto);
            loginResponseDto.setToken(token);

            return ResponseEntity.ok(loginResponseDto);
        } else{
            throw new RuntimeException("Authentication Failed");
        }
    }

    @GetMapping("/user/id/{id}")
    public ApiResponseDto getUserById(
            @PathVariable(name = "id")
            Long id
    ) {
        User user = authService.getUserById(id);

        UserDto userDto = UserMapper.toUserDtoFromUser(user);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("User Fetched Successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(userDto);

        return apiResponseDto;
    }

    @GetMapping("/user/email/{email}")
    public ApiResponseDto getUserByEmail(
            @PathVariable(name = "email")
            String email
    ) {
        User user = authService.getUserByEmail(email);

        UserDto userDto = UserMapper.toUserDtoFromUser(user);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("User Fetched Successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(userDto);

        return apiResponseDto;
    }

    @GetMapping("/validate")
    public String validateToken(
            @RequestParam("token")
            String token
    ) {
        authService.validateToken(token);
        return "Token is valid";
    }

}
