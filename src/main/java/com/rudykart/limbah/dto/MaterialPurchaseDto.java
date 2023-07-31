package com.rudykart.limbah.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MaterialPurchaseDto {
    @NotNull
    private Integer quantity;
    private String statusPurchase;
    private Long materialId;

    public MaterialPurchaseDto() {
    }

    public MaterialPurchaseDto(Integer quantity, String statusPurchase, Long materialId) {
        this.quantity = quantity;
        this.statusPurchase = statusPurchase;
        this.materialId = materialId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatusPurchase() {
        return statusPurchase;
    }

    public void setStatusPurchase(String statusPurchase) {
        this.statusPurchase = statusPurchase;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
}
