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

/**
 * Criteria class for the Category entity. This class is used in CategoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /categories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter category;

    private LongFilter assetId;

    private LongFilter bankAccountId;

    private LongFilter depreciationId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCategory() {
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public LongFilter getAssetId() {
        return assetId;
    }

    public void setAssetId(LongFilter assetId) {
        this.assetId = assetId;
    }

    public LongFilter getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(LongFilter bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public LongFilter getDepreciationId() {
        return depreciationId;
    }

    public void setDepreciationId(LongFilter depreciationId) {
        this.depreciationId = depreciationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryCriteria that = (CategoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(category, that.category) &&
            Objects.equals(assetId, that.assetId) &&
            Objects.equals(bankAccountId, that.bankAccountId) &&
            Objects.equals(depreciationId, that.depreciationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        category,
        assetId,
        bankAccountId,
        depreciationId
        );
    }

    @Override
    public String toString() {
        return "CategoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (assetId != null ? "assetId=" + assetId + ", " : "") +
                (bankAccountId != null ? "bankAccountId=" + bankAccountId + ", " : "") +
                (depreciationId != null ? "depreciationId=" + depreciationId + ", " : "") +
            "}";
    }

}
