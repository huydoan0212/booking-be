package com.example.nikebe.domain.auth.service;


import com.example.nikebe.config.security.jwt.JwtUtils;
import com.example.nikebe.domain.auth.dto.AuthResponse;
import com.example.nikebe.domain.auth.dto.LoginAdminDTO;
import com.example.nikebe.domain.user.user.UserDetailsImplement;
import com.example.nikebe.domain.user.user.entity.UserEntity;
import com.example.nikebe.domain.user.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse authenticate(LoginAdminDTO dto) throws GeneralSecurityException, IOException {
        Optional<UserEntity> user = userRepository.findByUsername(dto.getUsername());
        if(user.isEmpty() || !bCryptPasswordEncoder.matches(dto.getPassword(), user.get().getPassword()) ) throw new BadCredentialsException("Username or password is incorrect", new Throwable("username and password"));
        UserDetails userDetails = new UserDetailsImplement(user.get());
        String token = jwtUtils.generateToken(userDetails);
        return new AuthResponse(token, jwtUtils.getExpirationTime(), user.get().getUserRoleToString());
    }

}
