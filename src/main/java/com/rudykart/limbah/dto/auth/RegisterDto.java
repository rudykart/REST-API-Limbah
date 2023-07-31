package com.rudykart.limbah.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterDto {
    @NotBlank
    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String name;

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String email;

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(max = 200)
    private String password;

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
