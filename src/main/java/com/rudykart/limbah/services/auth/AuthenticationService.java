package com.rudykart.limbah.services.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rudykart.limbah.dto.auth.AuthenticationDto;
import com.rudykart.limbah.dto.auth.AuthenticationResponse;
import com.rudykart.limbah.dto.auth.RegisterDto;
import com.rudykart.limbah.dto.auth.TokenDto;
import com.rudykart.limbah.entities.user.Role;
import com.rudykart.limbah.entities.user.Token;
import com.rudykart.limbah.entities.user.User;
import com.rudykart.limbah.exceptions.AuthenticationFailedException;
import com.rudykart.limbah.repositories.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            TokenService tokenService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> register(RegisterDto registerDto) {
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(Role.valueOf(registerDto.getRole()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    public AuthenticationResponse authenticate(AuthenticationDto authenticationDto) {
        System.out.println("Auth Service = Mulai ");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationDto.getEmail(),
                            authenticationDto.getPassword()));
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Your credential not match");
        }
        System.out.println("Auth Service = Selesai ");

        return new AuthenticationResponse(jwtService.generateToken(authenticationDto.getEmail()),
                tokenService.createToken(authenticationDto.getEmail()).getToken());
    }

    public AuthenticationResponse refreshToken(TokenDto tokenDto) {
        // AuthenticationResponse token = new AuthenticationResponse();
        return tokenService.findByToken(tokenDto.getToken())
                .map(tokenService::verifyExpiration)
                .map(Token::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getEmail());
                    return new AuthenticationResponse(accessToken,
                            tokenService.createToken(user.getEmail()).getToken());
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));

    }
}
