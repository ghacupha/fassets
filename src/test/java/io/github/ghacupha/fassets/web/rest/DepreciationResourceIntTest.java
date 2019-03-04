package io.github.ghacupha.fassets.web.rest;

import io.github.ghacupha.fassets.FassetsApp;

import io.github.ghacupha.fassets.domain.Depreciation;
import io.github.ghacupha.fassets.domain.Category;
import io.github.ghacupha.fassets.repository.DepreciationRepository;
import io.github.ghacupha.fassets.service.DepreciationService;
import io.github.ghacupha.fassets.service.dto.DepreciationDTO;
import io.github.ghacupha.fassets.service.mapper.DepreciationMapper;
import io.github.ghacupha.fassets.web.rest.errors.ExceptionTranslator;
import io.github.ghacupha.fassets.service.dto.DepreciationCriteria;
import io.github.ghacupha.fassets.service.DepreciationQueryService;

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
 * Test class for the DepreciationResource REST controller.
 *
 * @see DepreciationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FassetsApp.class)
public class DepreciationResourceIntTest {

    private static final String DEFAULT_TYPE_OF_DEPRECIATION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_OF_DEPRECIATION = "BBBBBBBBBB";

    @Autowired
    private DepreciationRepository depreciationRepository;

    @Autowired
    private DepreciationMapper depreciationMapper;

    @Autowired
    private DepreciationService depreciationService;

    @Autowired
    private DepreciationQueryService depreciationQueryService;

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

    private MockMvc restDepreciationMockMvc;

    private Depreciation depreciation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepreciationResource depreciationResource = new DepreciationResource(depreciationService, depreciationQueryService);
        this.restDepreciationMockMvc = MockMvcBuilders.standaloneSetup(depreciationResource)
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
    public static Depreciation createEntity(EntityManager em) {
        Depreciation depreciation = new Depreciation()
            .typeOfDepreciation(DEFAULT_TYPE_OF_DEPRECIATION);
        return depreciation;
    }

    @Before
    public void initTest() {
        depreciation = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepreciation() throws Exception {
        int databaseSizeBeforeCreate = depreciationRepository.findAll().size();

        // Create the Depreciation
        DepreciationDTO depreciationDTO = depreciationMapper.toDto(depreciation);
        restDepreciationMockMvc.perform(post("/api/depreciations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationDTO)))
            .andExpect(status().isCreated());

        // Validate the Depreciation in the database
        List<Depreciation> depreciationList = depreciationRepository.findAll();
        assertThat(depreciationList).hasSize(databaseSizeBeforeCreate + 1);
        Depreciation testDepreciation = depreciationList.get(depreciationList.size() - 1);
        assertThat(testDepreciation.getTypeOfDepreciation()).isEqualTo(DEFAULT_TYPE_OF_DEPRECIATION);
    }

    @Test
    @Transactional
    public void createDepreciationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depreciationRepository.findAll().size();

        // Create the Depreciation with an existing ID
        depreciation.setId(1L);
        DepreciationDTO depreciationDTO = depreciationMapper.toDto(depreciation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationMockMvc.perform(post("/api/depreciations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Depreciation in the database
        List<Depreciation> depreciationList = depreciationRepository.findAll();
        assertThat(depreciationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeOfDepreciationIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationRepository.findAll().size();
        // set the field null
        depreciation.setTypeOfDepreciation(null);

        // Create the Depreciation, which fails.
        DepreciationDTO depreciationDTO = depreciationMapper.toDto(depreciation);

        restDepreciationMockMvc.perform(post("/api/depreciations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationDTO)))
            .andExpect(status().isBadRequest());

        List<Depreciation> depreciationList = depreciationRepository.findAll();
        assertThat(depreciationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepreciations() throws Exception {
        // Initialize the database
        depreciationRepository.saveAndFlush(depreciation);

        // Get all the depreciationList
        restDepreciationMockMvc.perform(get("/api/depreciations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeOfDepreciation").value(hasItem(DEFAULT_TYPE_OF_DEPRECIATION.toString())));
    }
    
    @Test
    @Transactional
    public void getDepreciation() throws Exception {
        // Initialize the database
        depreciationRepository.saveAndFlush(depreciation);

        // Get the depreciation
        restDepreciationMockMvc.perform(get("/api/depreciations/{id}", depreciation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(depreciation.getId().intValue()))
            .andExpect(jsonPath("$.typeOfDepreciation").value(DEFAULT_TYPE_OF_DEPRECIATION.toString()));
    }

    @Test
    @Transactional
    public void getAllDepreciationsByTypeOfDepreciationIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRepository.saveAndFlush(depreciation);

        // Get all the depreciationList where typeOfDepreciation equals to DEFAULT_TYPE_OF_DEPRECIATION
        defaultDepreciationShouldBeFound("typeOfDepreciation.equals=" + DEFAULT_TYPE_OF_DEPRECIATION);

        // Get all the depreciationList where typeOfDepreciation equals to UPDATED_TYPE_OF_DEPRECIATION
        defaultDepreciationShouldNotBeFound("typeOfDepreciation.equals=" + UPDATED_TYPE_OF_DEPRECIATION);
    }

    @Test
    @Transactional
    public void getAllDepreciationsByTypeOfDepreciationIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationRepository.saveAndFlush(depreciation);

        // Get all the depreciationList where typeOfDepreciation in DEFAULT_TYPE_OF_DEPRECIATION or UPDATED_TYPE_OF_DEPRECIATION
        defaultDepreciationShouldBeFound("typeOfDepreciation.in=" + DEFAULT_TYPE_OF_DEPRECIATION + "," + UPDATED_TYPE_OF_DEPRECIATION);

        // Get all the depreciationList where typeOfDepreciation equals to UPDATED_TYPE_OF_DEPRECIATION
        defaultDepreciationShouldNotBeFound("typeOfDepreciation.in=" + UPDATED_TYPE_OF_DEPRECIATION);
    }

    @Test
    @Transactional
    public void getAllDepreciationsByTypeOfDepreciationIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationRepository.saveAndFlush(depreciation);

        // Get all the depreciationList where typeOfDepreciation is not null
        defaultDepreciationShouldBeFound("typeOfDepreciation.specified=true");

        // Get all the depreciationList where typeOfDepreciation is null
        defaultDepreciationShouldNotBeFound("typeOfDepreciation.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepreciationsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        depreciation.setCategory(category);
        category.setDepreciation(depreciation);
        depreciationRepository.saveAndFlush(depreciation);
        Long categoryId = category.getId();

        // Get all the depreciationList where category equals to categoryId
        defaultDepreciationShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the depreciationList where category equals to categoryId + 1
        defaultDepreciationShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDepreciationShouldBeFound(String filter) throws Exception {
        restDepreciationMockMvc.perform(get("/api/depreciations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeOfDepreciation").value(hasItem(DEFAULT_TYPE_OF_DEPRECIATION)));

        // Check, that the count call also returns 1
        restDepreciationMockMvc.perform(get("/api/depreciations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDepreciationShouldNotBeFound(String filter) throws Exception {
        restDepreciationMockMvc.perform(get("/api/depreciations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationMockMvc.perform(get("/api/depreciations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDepreciation() throws Exception {
        // Get the depreciation
        restDepreciationMockMvc.perform(get("/api/depreciations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepreciation() throws Exception {
        // Initialize the database
        depreciationRepository.saveAndFlush(depreciation);

        int databaseSizeBeforeUpdate = depreciationRepository.findAll().size();

        // Update the depreciation
        Depreciation updatedDepreciation = depreciationRepository.findById(depreciation.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciation are not directly saved in db
        em.detach(updatedDepreciation);
        updatedDepreciation
            .typeOfDepreciation(UPDATED_TYPE_OF_DEPRECIATION);
        DepreciationDTO depreciationDTO = depreciationMapper.toDto(updatedDepreciation);

        restDepreciationMockMvc.perform(put("/api/depreciations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationDTO)))
            .andExpect(status().isOk());

        // Validate the Depreciation in the database
        List<Depreciation> depreciationList = depreciationRepository.findAll();
        assertThat(depreciationList).hasSize(databaseSizeBeforeUpdate);
        Depreciation testDepreciation = depreciationList.get(depreciationList.size() - 1);
        assertThat(testDepreciation.getTypeOfDepreciation()).isEqualTo(UPDATED_TYPE_OF_DEPRECIATION);
    }

    @Test
    @Transactional
    public void updateNonExistingDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = depreciationRepository.findAll().size();

        // Create the Depreciation
        DepreciationDTO depreciationDTO = depreciationMapper.toDto(depreciation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationMockMvc.perform(put("/api/depreciations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Depreciation in the database
        List<Depreciation> depreciationList = depreciationRepository.findAll();
        assertThat(depreciationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDepreciation() throws Exception {
        // Initialize the database
        depreciationRepository.saveAndFlush(depreciation);

        int databaseSizeBeforeDelete = depreciationRepository.findAll().size();

        // Delete the depreciation
        restDepreciationMockMvc.perform(delete("/api/depreciations/{id}", depreciation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Depreciation> depreciationList = depreciationRepository.findAll();
        assertThat(depreciationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Depreciation.class);
        Depreciation depreciation1 = new Depreciation();
        depreciation1.setId(1L);
        Depreciation depreciation2 = new Depreciation();
        depreciation2.setId(depreciation1.getId());
        assertThat(depreciation1).isEqualTo(depreciation2);
        depreciation2.setId(2L);
        assertThat(depreciation1).isNotEqualTo(depreciation2);
        depreciation1.setId(null);
        assertThat(depreciation1).isNotEqualTo(depreciation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationDTO.class);
        DepreciationDTO depreciationDTO1 = new DepreciationDTO();
        depreciationDTO1.setId(1L);
        DepreciationDTO depreciationDTO2 = new DepreciationDTO();
        assertThat(depreciationDTO1).isNotEqualTo(depreciationDTO2);
        depreciationDTO2.setId(depreciationDTO1.getId());
        assertThat(depreciationDTO1).isEqualTo(depreciationDTO2);
        depreciationDTO2.setId(2L);
        assertThat(depreciationDTO1).isNotEqualTo(depreciationDTO2);
        depreciationDTO1.setId(null);
        assertThat(depreciationDTO1).isNotEqualTo(depreciationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(depreciationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(depreciationMapper.fromId(null)).isNull();
    }
}
