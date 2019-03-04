package io.github.ghacupha.fassets.service.mapper;

import io.github.ghacupha.fassets.domain.*;
import io.github.ghacupha.fassets.service.dto.AssetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Asset and its DTO AssetDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ServiceOutletMapper.class})
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "serviceOutlet.id", target = "serviceOutletId")
    AssetDTO toDto(Asset asset);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "serviceOutletId", target = "serviceOutlet")
    Asset toEntity(AssetDTO assetDTO);

    default Asset fromId(Long id) {
        if (id == null) {
            return null;
        }
        Asset asset = new Asset();
        asset.setId(id);
        return asset;
    }
}
