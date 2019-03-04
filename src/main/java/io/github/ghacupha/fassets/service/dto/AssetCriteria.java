package io.github.ghacupha.fassets.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Asset entity. This class is used in AssetResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /assets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LocalDateFilter purchaseDate;

    private StringFilter assetTag;

    private BigDecimalFilter purchaseCost;

    private LongFilter categoryId;

    private LongFilter serviceOutletId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public StringFilter getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(StringFilter assetTag) {
        this.assetTag = assetTag;
    }

    public BigDecimalFilter getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimalFilter purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getServiceOutletId() {
        return serviceOutletId;
    }

    public void setServiceOutletId(LongFilter serviceOutletId) {
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
        final AssetCriteria that = (AssetCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(purchaseCost, that.purchaseCost) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        purchaseDate,
        assetTag,
        purchaseCost,
        categoryId,
        serviceOutletId
        );
    }

    @Override
    public String toString() {
        return "AssetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
                (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
                (purchaseCost != null ? "purchaseCost=" + purchaseCost + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            "}";
    }

}
