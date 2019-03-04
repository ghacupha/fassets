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
 * Criteria class for the Depreciation entity. This class is used in DepreciationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /depreciations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter typeOfDepreciation;

    private LongFilter categoryId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTypeOfDepreciation() {
        return typeOfDepreciation;
    }

    public void setTypeOfDepreciation(StringFilter typeOfDepreciation) {
        this.typeOfDepreciation = typeOfDepreciation;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DepreciationCriteria that = (DepreciationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(typeOfDepreciation, that.typeOfDepreciation) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        typeOfDepreciation,
        categoryId
        );
    }

    @Override
    public String toString() {
        return "DepreciationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (typeOfDepreciation != null ? "typeOfDepreciation=" + typeOfDepreciation + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            "}";
    }

}
