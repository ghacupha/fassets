package io.github.ghacupha.fassets.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the Asset entity.
 */
public class AssetDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private LocalDate purchaseDate;

    @NotNull
    private String assetTag;

    @NotNull
    private BigDecimal purchaseCost;


    private Long categoryId;

    private Long serviceOutletId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getServiceOutletId() {
        return serviceOutletId;
    }

    public void setServiceOutletId(Long serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AssetDTO assetDTO = (AssetDTO) o;
        if (assetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AssetDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", purchaseCost=" + getPurchaseCost() +
            ", category=" + getCategoryId() +
            ", serviceOutlet=" + getServiceOutletId() +
            "}";
    }
}
