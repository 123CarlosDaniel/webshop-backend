package com.backend.services.impl;

import com.backend.exception.CustomExceptions.DuplicateInstanceException;
import com.backend.models.entities.Admin;
import com.backend.repository.AdminRepository;
import com.backend.request.AuthenticationRequest;
import com.backend.request.RegisterRequest;
import com.backend.response.AuthenticationResponse;
import com.backend.security.utils.JwtUtils;
import com.backend.services.interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);
        Admin admin = adminRepository.findAdminByUsername(authenticationRequest.getUsername()).get();
        String token = jwtUtils.generateJwt(admin);
        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var foundAdmin = adminRepository.findAdminByUsername(registerRequest.getUsername());
        if (foundAdmin.isPresent()) throw new DuplicateInstanceException("Username is already used", HttpStatus.BAD_REQUEST);

        Admin admin = Admin.builder()
                .fullName(registerRequest.getFullName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        adminRepository.save(admin);
        String token = jwtUtils.generateJwt(admin);
        return  new AuthenticationResponse(token);
    }
}
