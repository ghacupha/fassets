package io.github.ghacupha.fassets.web.rest;

import io.github.ghacupha.fassets.FassetsApp;

import io.github.ghacupha.fassets.domain.ServiceOutlet;
import io.github.ghacupha.fassets.domain.Asset;
import io.github.ghacupha.fassets.repository.ServiceOutletRepository;
import io.github.ghacupha.fassets.service.ServiceOutletService;
import io.github.ghacupha.fassets.service.dto.ServiceOutletDTO;
import io.github.ghacupha.fassets.service.mapper.ServiceOutletMapper;
import io.github.ghacupha.fassets.web.rest.errors.ExceptionTranslator;
import io.github.ghacupha.fassets.service.dto.ServiceOutletCriteria;
import io.github.ghacupha.fassets.service.ServiceOutletQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static io.github.ghacupha.fassets.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ServiceOutletResource REST controller.
 *
 * @see ServiceOutletResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FassetsApp.class)
public class ServiceOutletResourceIntTest {

    private static final String DEFAULT_SERVICE_OUTLET = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBB";

    @Autowired
    private ServiceOutletRepository serviceOutletRepository;

    @Autowired
    private ServiceOutletMapper serviceOutletMapper;

    @Autowired
    private ServiceOutletService serviceOutletService;

    @Autowired
    private ServiceOutletQueryService serviceOutletQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restServiceOutletMockMvc;

    private ServiceOutlet serviceOutlet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceOutletResource serviceOutletResource = new ServiceOutletResource(serviceOutletService, serviceOutletQueryService);
        this.restServiceOutletMockMvc = MockMvcBuilders.standaloneSetup(serviceOutletResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOutlet createEntity(EntityManager em) {
        ServiceOutlet serviceOutlet = new ServiceOutlet()
            .serviceOutlet(DEFAULT_SERVICE_OUTLET)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE);
        return serviceOutlet;
    }

    @Before
    public void initTest() {
        serviceOutlet = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceOutlet() throws Exception {
        int databaseSizeBeforeCreate = serviceOutletRepository.findAll().size();

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);
        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getServiceOutlet()).isEqualTo(DEFAULT_SERVICE_OUTLET);
        assertThat(testServiceOutlet.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void createServiceOutletWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceOutletRepository.findAll().size();

        // Create the ServiceOutlet with an existing ID
        serviceOutlet.setId(1L);
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkServiceOutletIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceOutletRepository.findAll().size();
        // set the field null
        serviceOutlet.setServiceOutlet(null);

        // Create the ServiceOutlet, which fails.
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceOutletCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceOutletRepository.findAll().size();
        // set the field null
        serviceOutlet.setServiceOutletCode(null);

        // Create the ServiceOutlet, which fails.
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceOutlets() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList
        restServiceOutletMockMvc.perform(get("/api/service-outlets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET.toString())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE.toString())));
    }
    
    @Test
    @Transactional
    public void getServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get the serviceOutlet
        restServiceOutletMockMvc.perform(get("/api/service-outlets/{id}", serviceOutlet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceOutlet.getId().intValue()))
            .andExpect(jsonPath("$.serviceOutlet").value(DEFAULT_SERVICE_OUTLET.toString()))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE.toString()));
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutlet equals to DEFAULT_SERVICE_OUTLET
        defaultServiceOutletShouldBeFound("serviceOutlet.equals=" + DEFAULT_SERVICE_OUTLET);

        // Get all the serviceOutletList where serviceOutlet equals to UPDATED_SERVICE_OUTLET
        defaultServiceOutletShouldNotBeFound("serviceOutlet.equals=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutlet in DEFAULT_SERVICE_OUTLET or UPDATED_SERVICE_OUTLET
        defaultServiceOutletShouldBeFound("serviceOutlet.in=" + DEFAULT_SERVICE_OUTLET + "," + UPDATED_SERVICE_OUTLET);

        // Get all the serviceOutletList where serviceOutlet equals to UPDATED_SERVICE_OUTLET
        defaultServiceOutletShouldNotBeFound("serviceOutlet.in=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutlet is not null
        defaultServiceOutletShouldBeFound("serviceOutlet.specified=true");

        // Get all the serviceOutletList where serviceOutlet is null
        defaultServiceOutletShouldNotBeFound("serviceOutlet.specified=false");
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the serviceOutletList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldBeFound("serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE);

        // Get all the serviceOutletList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode is not null
        defaultServiceOutletShouldBeFound("serviceOutletCode.specified=true");

        // Get all the serviceOutletList where serviceOutletCode is null
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByAssetIsEqualToSomething() throws Exception {
        // Initialize the database
        Asset asset = AssetResourceIntTest.createEntity(em);
        em.persist(asset);
        em.flush();
        serviceOutlet.addAsset(asset);
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        Long assetId = asset.getId();

        // Get all the serviceOutletList where asset equals to assetId
        defaultServiceOutletShouldBeFound("assetId.equals=" + assetId);

        // Get all the serviceOutletList where asset equals to assetId + 1
        defaultServiceOutletShouldNotBeFound("assetId.equals=" + (assetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultServiceOutletShouldBeFound(String filter) throws Exception {
        restServiceOutletMockMvc.perform(get("/api/service-outlets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)));

        // Check, that the count call also returns 1
        restServiceOutletMockMvc.perform(get("/api/service-outlets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultServiceOutletShouldNotBeFound(String filter) throws Exception {
        restServiceOutletMockMvc.perform(get("/api/service-outlets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceOutletMockMvc.perform(get("/api/service-outlets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingServiceOutlet() throws Exception {
        // Get the serviceOutlet
        restServiceOutletMockMvc.perform(get("/api/service-outlets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();

        // Update the serviceOutlet
        ServiceOutlet updatedServiceOutlet = serviceOutletRepository.findById(serviceOutlet.getId()).get();
        // Disconnect from session so that the updates on updatedServiceOutlet are not directly saved in db
        em.detach(updatedServiceOutlet);
        updatedServiceOutlet
            .serviceOutlet(UPDATED_SERVICE_OUTLET)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE);
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(updatedServiceOutlet);

        restServiceOutletMockMvc.perform(put("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getServiceOutlet()).isEqualTo(UPDATED_SERVICE_OUTLET);
        assertThat(testServiceOutlet.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc.perform(put("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeDelete = serviceOutletRepository.findAll().size();

        // Delete the serviceOutlet
        restServiceOutletMockMvc.perform(delete("/api/service-outlets/{id}", serviceOutlet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOutlet.class);
        ServiceOutlet serviceOutlet1 = new ServiceOutlet();
        serviceOutlet1.setId(1L);
        ServiceOutlet serviceOutlet2 = new ServiceOutlet();
        serviceOutlet2.setId(serviceOutlet1.getId());
        assertThat(serviceOutlet1).isEqualTo(serviceOutlet2);
        serviceOutlet2.setId(2L);
        assertThat(serviceOutlet1).isNotEqualTo(serviceOutlet2);
        serviceOutlet1.setId(null);
        assertThat(serviceOutlet1).isNotEqualTo(serviceOutlet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOutletDTO.class);
        ServiceOutletDTO serviceOutletDTO1 = new ServiceOutletDTO();
        serviceOutletDTO1.setId(1L);
        ServiceOutletDTO serviceOutletDTO2 = new ServiceOutletDTO();
        assertThat(serviceOutletDTO1).isNotEqualTo(serviceOutletDTO2);
        serviceOutletDTO2.setId(serviceOutletDTO1.getId());
        assertThat(serviceOutletDTO1).isEqualTo(serviceOutletDTO2);
        serviceOutletDTO2.setId(2L);
        assertThat(serviceOutletDTO1).isNotEqualTo(serviceOutletDTO2);
        serviceOutletDTO1.setId(null);
        assertThat(serviceOutletDTO1).isNotEqualTo(serviceOutletDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceOutletMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(serviceOutletMapper.fromId(null)).isNull();
    }
}
