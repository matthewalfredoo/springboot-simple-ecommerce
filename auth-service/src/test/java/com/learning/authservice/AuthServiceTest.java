package com.learning.authservice;


import com.learning.authservice.entity.User;
import com.learning.authservice.repository.UserRepository;
import com.learning.authservice.service.JwtService;
import com.learning.authservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setName("test");
        user.setAddress("test address");
        user.setEmail("test@email.com");
        user.setPassword("test");
        user.setRole(User.ROLE_CUSTOMER);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = authService.saveUser(user);

        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getAddress(), savedUser.getAddress());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(user.getRole(), savedUser.getRole());
    }

    @Test
    public void testGenerateToken() {
        when(jwtService.generateToken(1L, "test", User.ROLE_CUSTOMER)).thenReturn("token");

        String token = authService.generateToken(1L, "test", User.ROLE_CUSTOMER);

        assertEquals("token", token);
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setAddress("test address");
        user.setEmail("test@email.com");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User savedUser = authService.getUserById(1L);
        assertEquals(1L, savedUser.getId());
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setAddress("test address");
        user.setEmail("test@email.com");

        when(userRepository.findByEmail("test@email.com")).thenReturn(
                java.util.Optional.of(user)
        );

        User savedUser = authService.getUserByEmail(user.getEmail());
        assertEquals("test@email.com", savedUser.getEmail());
    }

}
