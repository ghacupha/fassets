package io.github.ghacupha.fassets.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.ghacupha.fassets.domain.Depreciation;
import io.github.ghacupha.fassets.domain.*; // for static metamodels
import io.github.ghacupha.fassets.repository.DepreciationRepository;
import io.github.ghacupha.fassets.service.dto.DepreciationCriteria;
import io.github.ghacupha.fassets.service.dto.DepreciationDTO;
import io.github.ghacupha.fassets.service.mapper.DepreciationMapper;

/**
 * Service for executing complex queries for Depreciation entities in the database.
 * The main input is a {@link DepreciationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationDTO} or a {@link Page} of {@link DepreciationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationQueryService extends QueryService<Depreciation> {

    private final Logger log = LoggerFactory.getLogger(DepreciationQueryService.class);

    private final DepreciationRepository depreciationRepository;

    private final DepreciationMapper depreciationMapper;

    public DepreciationQueryService(DepreciationRepository depreciationRepository, DepreciationMapper depreciationMapper) {
        this.depreciationRepository = depreciationRepository;
        this.depreciationMapper = depreciationMapper;
    }

    /**
     * Return a {@link List} of {@link DepreciationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationDTO> findByCriteria(DepreciationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Depreciation> specification = createSpecification(criteria);
        return depreciationMapper.toDto(depreciationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationDTO> findByCriteria(DepreciationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Depreciation> specification = createSpecification(criteria);
        return depreciationRepository.findAll(specification, page)
            .map(depreciationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Depreciation> specification = createSpecification(criteria);
        return depreciationRepository.count(specification);
    }

    /**
     * Function to convert DepreciationCriteria to a {@link Specification}
     */
    private Specification<Depreciation> createSpecification(DepreciationCriteria criteria) {
        Specification<Depreciation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Depreciation_.id));
            }
            if (criteria.getTypeOfDepreciation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeOfDepreciation(), Depreciation_.typeOfDepreciation));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Depreciation_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
