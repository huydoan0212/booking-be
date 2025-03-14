package com.example.booking.domain.auth;

import com.example.booking.domain.auth.dto.AuthResponse;
import com.example.booking.domain.auth.dto.LoginAdminDTO;
import com.example.booking.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {

        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public AuthResponse authenticate(@Valid @RequestBody LoginAdminDTO dto) throws GeneralSecurityException, IOException {
        return authenticationService.authenticate(dto);
    }

}
