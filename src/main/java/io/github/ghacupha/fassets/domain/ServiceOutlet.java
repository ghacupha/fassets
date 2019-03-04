package io.github.ghacupha.fassets.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ServiceOutlet.
 */
@Entity
@Table(name = "service_outlet")
public class ServiceOutlet implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "service_outlet", nullable = false)
    private String serviceOutlet;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "service_outlet_code", length = 3, nullable = false)
    private String serviceOutletCode;

    @OneToMany(mappedBy = "serviceOutlet")
    private Set<Asset> assets = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceOutlet() {
        return serviceOutlet;
    }

    public ServiceOutlet serviceOutlet(String serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
        return this;
    }

    public void setServiceOutlet(String serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public String getServiceOutletCode() {
        return serviceOutletCode;
    }

    public ServiceOutlet serviceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public ServiceOutlet assets(Set<Asset> assets) {
        this.assets = assets;
        return this;
    }

    public ServiceOutlet addAsset(Asset asset) {
        this.assets.add(asset);
        asset.setServiceOutlet(this);
        return this;
    }

    public ServiceOutlet removeAsset(Asset asset) {
        this.assets.remove(asset);
        asset.setServiceOutlet(null);
        return this;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
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
        ServiceOutlet serviceOutlet = (ServiceOutlet) o;
        if (serviceOutlet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceOutlet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceOutlet{" +
            "id=" + getId() +
            ", serviceOutlet='" + getServiceOutlet() + "'" +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            "}";
    }
}
