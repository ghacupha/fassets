package io.github.ghacupha.fassets.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Depreciation entity.
 */
public class DepreciationDTO implements Serializable {

    private Long id;

    @NotNull
    private String typeOfDepreciation;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeOfDepreciation() {
        return typeOfDepreciation;
    }

    public void setTypeOfDepreciation(String typeOfDepreciation) {
        this.typeOfDepreciation = typeOfDepreciation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DepreciationDTO depreciationDTO = (DepreciationDTO) o;
        if (depreciationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), depreciationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DepreciationDTO{" +
            "id=" + getId() +
            ", typeOfDepreciation='" + getTypeOfDepreciation() + "'" +
            "}";
    }
}
