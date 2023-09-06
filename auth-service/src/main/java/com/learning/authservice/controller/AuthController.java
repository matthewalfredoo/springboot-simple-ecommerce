package com.learning.authservice.controller;

import com.learning.authservice.dto.AuthRequest;
import com.learning.authservice.entity.User;
import com.learning.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String registerUser(
            @RequestBody
            User user
    ) {
        return authService.saveUser(user);
    }

    @PostMapping("/login")
    public String login(
            @RequestBody
            AuthRequest authRequest
    ) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword()
                )
        );

        if(authenticate.isAuthenticated()) {
            return authService.generateToken(authRequest.getEmail());
        } else{
            throw new RuntimeException("Authentication Failed");
        }
    }

    @GetMapping("/user/{email}")
    public User getUserByEmail(
            @PathVariable(name = "email")
            String email
    ) {
        return authService.getUserByEmail(email);
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
