package com.rudykart.limbah.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthenticationResponse {
    private String accessToken;
    private String token;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String accessToken, String token) {
        this.accessToken = accessToken;
        this.token = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
