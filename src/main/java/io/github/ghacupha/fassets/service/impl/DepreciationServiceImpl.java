package io.github.ghacupha.fassets.service.impl;

import io.github.ghacupha.fassets.service.DepreciationService;
import io.github.ghacupha.fassets.domain.Depreciation;
import io.github.ghacupha.fassets.repository.DepreciationRepository;
import io.github.ghacupha.fassets.service.dto.DepreciationDTO;
import io.github.ghacupha.fassets.service.mapper.DepreciationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Depreciation.
 */
@Service
@Transactional
public class DepreciationServiceImpl implements DepreciationService {

    private final Logger log = LoggerFactory.getLogger(DepreciationServiceImpl.class);

    private final DepreciationRepository depreciationRepository;

    private final DepreciationMapper depreciationMapper;

    public DepreciationServiceImpl(DepreciationRepository depreciationRepository, DepreciationMapper depreciationMapper) {
        this.depreciationRepository = depreciationRepository;
        this.depreciationMapper = depreciationMapper;
    }

    /**
     * Save a depreciation.
     *
     * @param depreciationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DepreciationDTO save(DepreciationDTO depreciationDTO) {
        log.debug("Request to save Depreciation : {}", depreciationDTO);
        Depreciation depreciation = depreciationMapper.toEntity(depreciationDTO);
        depreciation = depreciationRepository.save(depreciation);
        return depreciationMapper.toDto(depreciation);
    }

    /**
     * Get all the depreciations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Depreciations");
        return depreciationRepository.findAll(pageable)
            .map(depreciationMapper::toDto);
    }



    /**
     *  get all the depreciations where Category is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<DepreciationDTO> findAllWhereCategoryIsNull() {
        log.debug("Request to get all depreciations where Category is null");
        return StreamSupport
            .stream(depreciationRepository.findAll().spliterator(), false)
            .filter(depreciation -> depreciation.getCategory() == null)
            .map(depreciationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one depreciation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationDTO> findOne(Long id) {
        log.debug("Request to get Depreciation : {}", id);
        return depreciationRepository.findById(id)
            .map(depreciationMapper::toDto);
    }

    /**
     * Delete the depreciation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Depreciation : {}", id);
        depreciationRepository.deleteById(id);
    }
}
