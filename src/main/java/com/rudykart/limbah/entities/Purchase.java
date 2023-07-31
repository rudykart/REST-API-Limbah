package com.rudykart.limbah.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rudykart.limbah.entities.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchases")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Purchase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPurchase statusPurchase;

    private Integer totalPaid;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<MaterialPurchase> materialPurchases = new HashSet<>();

    @ManyToOne
    private User user;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;

    public Purchase() {
    }

    public Purchase(Long id, StatusPurchase statusPurchase, Integer totalPaid, Customer customer,
            Set<MaterialPurchase> materialPurchases, User user, Date createdAt, Date updatedAt) {
        this.id = id;
        this.statusPurchase = statusPurchase;
        this.totalPaid = totalPaid;
        this.customer = customer;
        this.materialPurchases = materialPurchases;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusPurchase getStatusPurchase() {
        return statusPurchase;
    }

    public void setStatusPurchase(StatusPurchase statusPurchase) {
        this.statusPurchase = statusPurchase;
    }

    public Integer getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Integer totalPaid) {
        this.totalPaid = totalPaid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<MaterialPurchase> getMaterialPurchases() {
        return materialPurchases;
    }

    public void setMaterialPurchases(Set<MaterialPurchase> materialPurchases) {
        this.materialPurchases = materialPurchases;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
