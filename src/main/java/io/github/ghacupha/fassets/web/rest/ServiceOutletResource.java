package io.github.ghacupha.fassets.web.rest;
import io.github.ghacupha.fassets.service.ServiceOutletService;
import io.github.ghacupha.fassets.web.rest.errors.BadRequestAlertException;
import io.github.ghacupha.fassets.web.rest.util.HeaderUtil;
import io.github.ghacupha.fassets.web.rest.util.PaginationUtil;
import io.github.ghacupha.fassets.service.dto.ServiceOutletDTO;
import io.github.ghacupha.fassets.service.dto.ServiceOutletCriteria;
import io.github.ghacupha.fassets.service.ServiceOutletQueryService;
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

/**
 * REST controller for managing ServiceOutlet.
 */
@RestController
@RequestMapping("/api")
public class ServiceOutletResource {

    private final Logger log = LoggerFactory.getLogger(ServiceOutletResource.class);

    private static final String ENTITY_NAME = "serviceOutlet";

    private final ServiceOutletService serviceOutletService;

    private final ServiceOutletQueryService serviceOutletQueryService;

    public ServiceOutletResource(ServiceOutletService serviceOutletService, ServiceOutletQueryService serviceOutletQueryService) {
        this.serviceOutletService = serviceOutletService;
        this.serviceOutletQueryService = serviceOutletQueryService;
    }

    /**
     * POST  /service-outlets : Create a new serviceOutlet.
     *
     * @param serviceOutletDTO the serviceOutletDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceOutletDTO, or with status 400 (Bad Request) if the serviceOutlet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-outlets")
    public ResponseEntity<ServiceOutletDTO> createServiceOutlet(@Valid @RequestBody ServiceOutletDTO serviceOutletDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceOutlet : {}", serviceOutletDTO);
        if (serviceOutletDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceOutlet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceOutletDTO result = serviceOutletService.save(serviceOutletDTO);
        return ResponseEntity.created(new URI("/api/service-outlets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-outlets : Updates an existing serviceOutlet.
     *
     * @param serviceOutletDTO the serviceOutletDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceOutletDTO,
     * or with status 400 (Bad Request) if the serviceOutletDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceOutletDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-outlets")
    public ResponseEntity<ServiceOutletDTO> updateServiceOutlet(@Valid @RequestBody ServiceOutletDTO serviceOutletDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceOutlet : {}", serviceOutletDTO);
        if (serviceOutletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceOutletDTO result = serviceOutletService.save(serviceOutletDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceOutletDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-outlets : get all the serviceOutlets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of serviceOutlets in body
     */
    @GetMapping("/service-outlets")
    public ResponseEntity<List<ServiceOutletDTO>> getAllServiceOutlets(ServiceOutletCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ServiceOutlets by criteria: {}", criteria);
        Page<ServiceOutletDTO> page = serviceOutletQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-outlets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /service-outlets/count : count all the serviceOutlets.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/service-outlets/count")
    public ResponseEntity<Long> countServiceOutlets(ServiceOutletCriteria criteria) {
        log.debug("REST request to count ServiceOutlets by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceOutletQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /service-outlets/:id : get the "id" serviceOutlet.
     *
     * @param id the id of the serviceOutletDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceOutletDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-outlets/{id}")
    public ResponseEntity<ServiceOutletDTO> getServiceOutlet(@PathVariable Long id) {
        log.debug("REST request to get ServiceOutlet : {}", id);
        Optional<ServiceOutletDTO> serviceOutletDTO = serviceOutletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceOutletDTO);
    }

    /**
     * DELETE  /service-outlets/:id : delete the "id" serviceOutlet.
     *
     * @param id the id of the serviceOutletDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-outlets/{id}")
    public ResponseEntity<Void> deleteServiceOutlet(@PathVariable Long id) {
        log.debug("REST request to delete ServiceOutlet : {}", id);
        serviceOutletService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
