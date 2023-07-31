package com.rudykart.limbah.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SaleDto {
    private String description;
    private String buyerName;
    private Integer income;
    private List<Long> purchases;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public List<Long> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Long> purchases) {
        this.purchases = purchases;
    }

}
