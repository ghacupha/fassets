package io.github.ghacupha.fassets.service.mapper;

import io.github.ghacupha.fassets.domain.*;
import io.github.ghacupha.fassets.service.dto.CategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {BankAccountMapper.class, DepreciationMapper.class})
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    @Mapping(source = "depreciation.id", target = "depreciationId")
    CategoryDTO toDto(Category category);

    @Mapping(target = "assets", ignore = true)
    @Mapping(source = "bankAccountId", target = "bankAccount")
    @Mapping(source = "depreciationId", target = "depreciation")
    Category toEntity(CategoryDTO categoryDTO);

    default Category fromId(Long id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
