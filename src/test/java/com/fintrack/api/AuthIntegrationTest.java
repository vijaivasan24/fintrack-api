package com.fintrack.api;

import com.fintrack.api.controller.AuthController;
import com.fintrack.api.dto.AuthRequest;
import com.fintrack.api.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private AuthController authController;

    @Test
    void testRegisterAndLogin() {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("testuser");
        register.setEmail("testuser@example.com");
        register.setPassword("password");

        ResponseEntity<?> regResponse = authController.register(register);
        assertEquals(HttpStatus.OK, regResponse.getStatusCode());

        AuthRequest auth = new AuthRequest();
        auth.setEmail("testuser@example.com");
        auth.setPassword("password");

        ResponseEntity<?> loginResponse = authController.login(auth);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
    }
}
