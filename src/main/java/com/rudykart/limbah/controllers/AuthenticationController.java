package com.rudykart.limbah.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rudykart.limbah.dto.auth.AuthenticationDto;
import com.rudykart.limbah.dto.auth.AuthenticationResponse;
import com.rudykart.limbah.dto.auth.RegisterDto;
import com.rudykart.limbah.dto.auth.TokenDto;
import com.rudykart.limbah.services.auth.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationDto authenticationDto) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationDto));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody TokenDto tokenDto) {
        return ResponseEntity.ok(authenticationService.refreshToken(tokenDto));
    }

    @PostMapping("tes")
    public ResponseEntity<String> tes(@RequestBody String data) {
        return ResponseEntity.ok().body(data);
    }
}
