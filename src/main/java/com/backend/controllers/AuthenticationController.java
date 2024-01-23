package com.backend.controllers;

import com.backend.request.AuthenticationRequest;
import com.backend.request.RegisterRequest;
import com.backend.response.AuthenticationResponse;
import com.backend.services.interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {

        AuthenticationResponse token = authenticationService.register(registerRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse token = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(token);
    }
}
