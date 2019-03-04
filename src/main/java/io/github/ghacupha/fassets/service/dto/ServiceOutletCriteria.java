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
 * Criteria class for the ServiceOutlet entity. This class is used in ServiceOutletResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /service-outlets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServiceOutletCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serviceOutlet;

    private StringFilter serviceOutletCode;

    private LongFilter assetId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(StringFilter serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public StringFilter getServiceOutletCode() {
        return serviceOutletCode;
    }

    public void setServiceOutletCode(StringFilter serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public LongFilter getAssetId() {
        return assetId;
    }

    public void setAssetId(LongFilter assetId) {
        this.assetId = assetId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServiceOutletCriteria that = (ServiceOutletCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(serviceOutlet, that.serviceOutlet) &&
            Objects.equals(serviceOutletCode, that.serviceOutletCode) &&
            Objects.equals(assetId, that.assetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        serviceOutlet,
        serviceOutletCode,
        assetId
        );
    }

    @Override
    public String toString() {
        return "ServiceOutletCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (serviceOutlet != null ? "serviceOutlet=" + serviceOutlet + ", " : "") +
                (serviceOutletCode != null ? "serviceOutletCode=" + serviceOutletCode + ", " : "") +
                (assetId != null ? "assetId=" + assetId + ", " : "") +
            "}";
    }

}
