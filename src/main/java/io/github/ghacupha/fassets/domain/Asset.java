package io.github.ghacupha.fassets.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @NotNull
    @Column(name = "asset_tag", nullable = false)
    private String assetTag;

    @NotNull
    @Column(name = "purchase_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal purchaseCost;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("assets")
    private Category category;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("assets")
    private ServiceOutlet serviceOutlet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Asset description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Asset purchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public Asset assetTag(String assetTag) {
        this.assetTag = assetTag;
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public Asset purchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
        return this;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public Category getCategory() {
        return category;
    }

    public Asset category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ServiceOutlet getServiceOutlet() {
        return serviceOutlet;
    }

    public Asset serviceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
        return this;
    }

    public void setServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Asset asset = (Asset) o;
        if (asset.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), asset.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Asset{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", purchaseCost=" + getPurchaseCost() +
            "}";
    }
}
