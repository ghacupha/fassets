package io.github.ghacupha.fassets.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ServiceOutlet entity.
 */
public class ServiceOutletDTO implements Serializable {

    private Long id;

    @NotNull
    private String serviceOutlet;

    @NotNull
    @Size(min = 3, max = 3)
    private String serviceOutletCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(String serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public String getServiceOutletCode() {
        return serviceOutletCode;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceOutletDTO serviceOutletDTO = (ServiceOutletDTO) o;
        if (serviceOutletDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceOutletDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceOutletDTO{" +
            "id=" + getId() +
            ", serviceOutlet='" + getServiceOutlet() + "'" +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            "}";
    }
}
