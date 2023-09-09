package com.learning.authservice.service;

import com.learning.authservice.entity.User;

public interface AuthService {

    User saveUser(User user);

    String generateToken(String username);

    void validateToken(String token);

    User getUserByEmail(String email);

}
