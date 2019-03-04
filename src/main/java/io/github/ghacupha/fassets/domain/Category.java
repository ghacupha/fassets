package io.github.ghacupha.fassets.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 7287278383269032512L;

    @Id
    private Long id;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @OneToMany(mappedBy = "category")
    private Set<Asset> assets = new HashSet<>();

    @OneToOne(optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private BankAccount bankAccount;

    @OneToOne(optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private Depreciation depreciation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public Category category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public Category assets(Set<Asset> assets) {
        this.assets = assets;
        return this;
    }

    public Category addAsset(Asset asset) {
        this.assets.add(asset);
        asset.setCategory(this);
        return this;
    }

    public Category removeAsset(Asset asset) {
        this.assets.remove(asset);
        asset.setCategory(null);
        return this;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Category bankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Depreciation getDepreciation() {
        return depreciation;
    }

    public Category depreciation(Depreciation depreciation) {
        this.depreciation = depreciation;
        return this;
    }

    public void setDepreciation(Depreciation depreciation) {
        this.depreciation = depreciation;
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
        Category category = (Category) o;
        if (category.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
