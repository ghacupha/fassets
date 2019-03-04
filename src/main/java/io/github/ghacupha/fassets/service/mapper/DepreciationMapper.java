package io.github.ghacupha.fassets.service.mapper;

import io.github.ghacupha.fassets.domain.*;
import io.github.ghacupha.fassets.service.dto.DepreciationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Depreciation and its DTO DepreciationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepreciationMapper extends EntityMapper<DepreciationDTO, Depreciation> {


    @Mapping(target = "category", ignore = true)
    Depreciation toEntity(DepreciationDTO depreciationDTO);

    default Depreciation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Depreciation depreciation = new Depreciation();
        depreciation.setId(id);
        return depreciation;
    }
}
