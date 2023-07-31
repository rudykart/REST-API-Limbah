package com.rudykart.limbah.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PurchaseDto {
    @NotNull
    private Long customerId;
    private Long userId;
    @Valid
    private List<MaterialPurchaseDto> materialPurchases;

    // Properti untuk menyimpan data MaterialPurchase yang ditambahkan
    private List<MaterialPurchaseDto> materialPurchasesToAdd;

    // Properti untuk menyimpan daftar ID MaterialPurchase yang akan dihapus
    private List<Long> materialPurchaseIdsToDelete;

    public PurchaseDto() {
    }

    public PurchaseDto(Long customerId, Long userId, List<MaterialPurchaseDto> materialPurchases,
            List<MaterialPurchaseDto> materialPurchasesToAdd, List<Long> materialPurchaseIdsToDelete) {
        this.customerId = customerId;
        this.userId = userId;
        this.materialPurchases = materialPurchases;
        this.materialPurchasesToAdd = materialPurchasesToAdd;
        this.materialPurchaseIdsToDelete = materialPurchaseIdsToDelete;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<MaterialPurchaseDto> getMaterialPurchases() {
        return materialPurchases;
    }

    public void setMaterialPurchases(List<MaterialPurchaseDto> materialPurchases) {
        this.materialPurchases = materialPurchases;
    }

    public List<MaterialPurchaseDto> getMaterialPurchasesToAdd() {
        return materialPurchasesToAdd;
    }

    public void setMaterialPurchasesToAdd(List<MaterialPurchaseDto> materialPurchasesToAdd) {
        this.materialPurchasesToAdd = materialPurchasesToAdd;
    }

    public List<Long> getMaterialPurchaseIdsToDelete() {
        return materialPurchaseIdsToDelete;
    }

    public void setMaterialPurchaseIdsToDelete(List<Long> materialPurchaseIdsToDelete) {
        this.materialPurchaseIdsToDelete = materialPurchaseIdsToDelete;
    }

}
