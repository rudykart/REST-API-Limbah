package com.rudykart.limbah.entities;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "material_purchase")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MaterialPurchase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer purchasePrice;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    private Material material;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    @JsonBackReference
    private Purchase purchase;

    @Transient
    private Integer totalPrice;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;

    public MaterialPurchase() {
    }

    public MaterialPurchase(Long id, Integer purchasePrice, Integer quantity, StatusPurchase statusPurchase,
            Material material,
            Purchase purchase, Date createdAt, Date updatedAt) {
        this.id = id;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.material = material;
        this.purchase = purchase;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Integer getTotalPrice() {
        return totalPrice = quantity * purchasePrice;
    }

    // public void setTotalPrice(Integer totalPrice) {
    // this.totalPrice = totalPrice;
    // }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}