package com.rudykart.limbah.services.auth;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rudykart.limbah.entities.user.Token;
import com.rudykart.limbah.repositories.TokenRepository;
import com.rudykart.limbah.repositories.UserRepository;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public Token createToken(String email) {
        Token token = new Token(UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + 1000 * 60 * 120),
                userRepository.findByEmail(email).get());
        return tokenRepository.save(token);
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Token verifyExpiration(Token token) {
        // if (token.getExpired().compareTo(Instant.now()) < 0) {
        if (token.getExpired().before(new Date(System.currentTimeMillis()))) {
            tokenRepository.delete(token);
            throw new RuntimeException(
                    token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
