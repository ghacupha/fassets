package io.github.ghacupha.fassets.service.mapper;

import io.github.ghacupha.fassets.domain.*;
import io.github.ghacupha.fassets.service.dto.ServiceOutletDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ServiceOutlet and its DTO ServiceOutletDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceOutletMapper extends EntityMapper<ServiceOutletDTO, ServiceOutlet> {


    @Mapping(target = "assets", ignore = true)
    ServiceOutlet toEntity(ServiceOutletDTO serviceOutletDTO);

    default ServiceOutlet fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceOutlet serviceOutlet = new ServiceOutlet();
        serviceOutlet.setId(id);
        return serviceOutlet;
    }
}
