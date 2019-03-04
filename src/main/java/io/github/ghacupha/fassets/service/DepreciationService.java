package io.github.ghacupha.fassets.service;

import io.github.ghacupha.fassets.service.dto.DepreciationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Depreciation.
 */
public interface DepreciationService {

    /**
     * Save a depreciation.
     *
     * @param depreciationDTO the entity to save
     * @return the persisted entity
     */
    DepreciationDTO save(DepreciationDTO depreciationDTO);

    /**
     * Get all the depreciations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DepreciationDTO> findAll(Pageable pageable);
    /**
     * Get all the DepreciationDTO where Category is null.
     *
     * @return the list of entities
     */
    List<DepreciationDTO> findAllWhereCategoryIsNull();


    /**
     * Get the "id" depreciation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DepreciationDTO> findOne(Long id);

    /**
     * Delete the "id" depreciation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
