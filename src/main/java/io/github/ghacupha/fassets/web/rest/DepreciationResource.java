package io.github.ghacupha.fassets.web.rest;
import io.github.ghacupha.fassets.service.DepreciationService;
import io.github.ghacupha.fassets.web.rest.errors.BadRequestAlertException;
import io.github.ghacupha.fassets.web.rest.util.HeaderUtil;
import io.github.ghacupha.fassets.web.rest.util.PaginationUtil;
import io.github.ghacupha.fassets.service.dto.DepreciationDTO;
import io.github.ghacupha.fassets.service.dto.DepreciationCriteria;
import io.github.ghacupha.fassets.service.DepreciationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Depreciation.
 */
@RestController
@RequestMapping("/api")
public class DepreciationResource {

    private final Logger log = LoggerFactory.getLogger(DepreciationResource.class);

    private static final String ENTITY_NAME = "depreciation";

    private final DepreciationService depreciationService;

    private final DepreciationQueryService depreciationQueryService;

    public DepreciationResource(DepreciationService depreciationService, DepreciationQueryService depreciationQueryService) {
        this.depreciationService = depreciationService;
        this.depreciationQueryService = depreciationQueryService;
    }

    /**
     * POST  /depreciations : Create a new depreciation.
     *
     * @param depreciationDTO the depreciationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new depreciationDTO, or with status 400 (Bad Request) if the depreciation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/depreciations")
    public ResponseEntity<DepreciationDTO> createDepreciation(@Valid @RequestBody DepreciationDTO depreciationDTO) throws URISyntaxException {
        log.debug("REST request to save Depreciation : {}", depreciationDTO);
        if (depreciationDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationDTO result = depreciationService.save(depreciationDTO);
        return ResponseEntity.created(new URI("/api/depreciations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /depreciations : Updates an existing depreciation.
     *
     * @param depreciationDTO the depreciationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated depreciationDTO,
     * or with status 400 (Bad Request) if the depreciationDTO is not valid,
     * or with status 500 (Internal Server Error) if the depreciationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/depreciations")
    public ResponseEntity<DepreciationDTO> updateDepreciation(@Valid @RequestBody DepreciationDTO depreciationDTO) throws URISyntaxException {
        log.debug("REST request to update Depreciation : {}", depreciationDTO);
        if (depreciationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepreciationDTO result = depreciationService.save(depreciationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, depreciationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /depreciations : get all the depreciations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of depreciations in body
     */
    @GetMapping("/depreciations")
    public ResponseEntity<List<DepreciationDTO>> getAllDepreciations(DepreciationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Depreciations by criteria: {}", criteria);
        Page<DepreciationDTO> page = depreciationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/depreciations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /depreciations/count : count all the depreciations.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/depreciations/count")
    public ResponseEntity<Long> countDepreciations(DepreciationCriteria criteria) {
        log.debug("REST request to count Depreciations by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /depreciations/:id : get the "id" depreciation.
     *
     * @param id the id of the depreciationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the depreciationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/depreciations/{id}")
    public ResponseEntity<DepreciationDTO> getDepreciation(@PathVariable Long id) {
        log.debug("REST request to get Depreciation : {}", id);
        Optional<DepreciationDTO> depreciationDTO = depreciationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationDTO);
    }

    /**
     * DELETE  /depreciations/:id : delete the "id" depreciation.
     *
     * @param id the id of the depreciationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/depreciations/{id}")
    public ResponseEntity<Void> deleteDepreciation(@PathVariable Long id) {
        log.debug("REST request to delete Depreciation : {}", id);
        depreciationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
