package io.github.ghacupha.fassets.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Depreciation.
 */
@Entity
@Table(name = "depreciation")
public class Depreciation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type_of_depreciation", nullable = false)
    private String typeOfDepreciation;

    @OneToOne(mappedBy = "depreciation")
    @JsonIgnore
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeOfDepreciation() {
        return typeOfDepreciation;
    }

    public Depreciation typeOfDepreciation(String typeOfDepreciation) {
        this.typeOfDepreciation = typeOfDepreciation;
        return this;
    }

    public void setTypeOfDepreciation(String typeOfDepreciation) {
        this.typeOfDepreciation = typeOfDepreciation;
    }

    public Category getCategory() {
        return category;
    }

    public Depreciation category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
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
        Depreciation depreciation = (Depreciation) o;
        if (depreciation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), depreciation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Depreciation{" +
            "id=" + getId() +
            ", typeOfDepreciation='" + getTypeOfDepreciation() + "'" +
            "}";
    }
}
