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

import io.github.ghacupha.fassets.domain.Asset;
import io.github.ghacupha.fassets.domain.*; // for static metamodels
import io.github.ghacupha.fassets.repository.AssetRepository;
import io.github.ghacupha.fassets.service.dto.AssetCriteria;
import io.github.ghacupha.fassets.service.dto.AssetDTO;
import io.github.ghacupha.fassets.service.mapper.AssetMapper;

/**
 * Service for executing complex queries for Asset entities in the database.
 * The main input is a {@link AssetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetDTO} or a {@link Page} of {@link AssetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetQueryService extends QueryService<Asset> {

    private final Logger log = LoggerFactory.getLogger(AssetQueryService.class);

    private final AssetRepository assetRepository;

    private final AssetMapper assetMapper;

    public AssetQueryService(AssetRepository assetRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
    }

    /**
     * Return a {@link List} of {@link AssetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetDTO> findByCriteria(AssetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Asset> specification = createSpecification(criteria);
        return assetMapper.toDto(assetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetDTO> findByCriteria(AssetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Asset> specification = createSpecification(criteria);
        return assetRepository.findAll(specification, page)
            .map(assetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Asset> specification = createSpecification(criteria);
        return assetRepository.count(specification);
    }

    /**
     * Function to convert AssetCriteria to a {@link Specification}
     */
    private Specification<Asset> createSpecification(AssetCriteria criteria) {
        Specification<Asset> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Asset_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Asset_.description));
            }
            if (criteria.getPurchaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseDate(), Asset_.purchaseDate));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), Asset_.assetTag));
            }
            if (criteria.getPurchaseCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseCost(), Asset_.purchaseCost));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Asset_.category, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getServiceOutletId() != null) {
                specification = specification.and(buildSpecification(criteria.getServiceOutletId(),
                    root -> root.join(Asset_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)));
            }
        }
        return specification;
    }
}
