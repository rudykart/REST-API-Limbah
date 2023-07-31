package com.rudykart.limbah.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MaterialDto {

    @NotBlank
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 999999999)
    private Integer price;

    @NotNull
    @NotEmpty
    private String status;

    public MaterialDto() {
    }

    public MaterialDto(String name,
            Integer price,
            String status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
