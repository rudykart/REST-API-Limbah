package com.rudykart.limbah.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerDto {

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min = 1, max = 15)
    private String phoneNumber;

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min = 1, max = 50)
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
