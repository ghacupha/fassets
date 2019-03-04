package io.github.ghacupha.fassets.web.rest;

import io.github.ghacupha.fassets.FassetsApp;

import io.github.ghacupha.fassets.domain.Asset;
import io.github.ghacupha.fassets.domain.Category;
import io.github.ghacupha.fassets.domain.ServiceOutlet;
import io.github.ghacupha.fassets.repository.AssetRepository;
import io.github.ghacupha.fassets.service.AssetService;
import io.github.ghacupha.fassets.service.dto.AssetDTO;
import io.github.ghacupha.fassets.service.mapper.AssetMapper;
import io.github.ghacupha.fassets.web.rest.errors.ExceptionTranslator;
import io.github.ghacupha.fassets.service.dto.AssetCriteria;
import io.github.ghacupha.fassets.service.AssetQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static io.github.ghacupha.fassets.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssetResource REST controller.
 *
 * @see AssetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FassetsApp.class)
public class AssetResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PURCHASE_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_PURCHASE_COST = new BigDecimal(2);

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetQueryService assetQueryService;

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

    private MockMvc restAssetMockMvc;

    private Asset asset;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AssetResource assetResource = new AssetResource(assetService, assetQueryService);
        this.restAssetMockMvc = MockMvcBuilders.standaloneSetup(assetResource)
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
    public static Asset createEntity(EntityManager em) {
        Asset asset = new Asset()
            .description(DEFAULT_DESCRIPTION)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .assetTag(DEFAULT_ASSET_TAG)
            .purchaseCost(DEFAULT_PURCHASE_COST);
        // Add required entity
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        asset.setCategory(category);
        // Add required entity
        ServiceOutlet serviceOutlet = ServiceOutletResourceIntTest.createEntity(em);
        em.persist(serviceOutlet);
        em.flush();
        asset.setServiceOutlet(serviceOutlet);
        return asset;
    }

    @Before
    public void initTest() {
        asset = createEntity(em);
    }

    @Test
    @Transactional
    public void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);
        restAssetMockMvc.perform(post("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isCreated());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate + 1);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAsset.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testAsset.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testAsset.getPurchaseCost()).isEqualTo(DEFAULT_PURCHASE_COST);
    }

    @Test
    @Transactional
    public void createAssetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // Create the Asset with an existing ID
        asset.setId(1L);
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetMockMvc.perform(post("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setDescription(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc.perform(post("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPurchaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setPurchaseDate(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc.perform(post("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssetTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setAssetTag(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc.perform(post("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPurchaseCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setPurchaseCost(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc.perform(post("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssets() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList
        restAssetMockMvc.perform(get("/api/assets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG.toString())))
            .andExpect(jsonPath("$.[*].purchaseCost").value(hasItem(DEFAULT_PURCHASE_COST.intValue())));
    }
    
    @Test
    @Transactional
    public void getAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(asset.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG.toString()))
            .andExpect(jsonPath("$.purchaseCost").value(DEFAULT_PURCHASE_COST.intValue()));
    }

    @Test
    @Transactional
    public void getAllAssetsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where description equals to DEFAULT_DESCRIPTION
        defaultAssetShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetList where description equals to UPDATED_DESCRIPTION
        defaultAssetShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssetsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetList where description equals to UPDATED_DESCRIPTION
        defaultAssetShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssetsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where description is not null
        defaultAssetShouldBeFound("description.specified=true");

        // Get all the assetList where description is null
        defaultAssetShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultAssetShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the assetList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultAssetShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultAssetShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the assetList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultAssetShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseDate is not null
        defaultAssetShouldBeFound("purchaseDate.specified=true");

        // Get all the assetList where purchaseDate is null
        defaultAssetShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseDate greater than or equals to DEFAULT_PURCHASE_DATE
        defaultAssetShouldBeFound("purchaseDate.greaterOrEqualThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the assetList where purchaseDate greater than or equals to UPDATED_PURCHASE_DATE
        defaultAssetShouldNotBeFound("purchaseDate.greaterOrEqualThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseDate less than or equals to DEFAULT_PURCHASE_DATE
        defaultAssetShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the assetList where purchaseDate less than or equals to UPDATED_PURCHASE_DATE
        defaultAssetShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }


    @Test
    @Transactional
    public void getAllAssetsByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetTag equals to DEFAULT_ASSET_TAG
        defaultAssetShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the assetList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllAssetsByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultAssetShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the assetList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllAssetsByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetTag is not null
        defaultAssetShouldBeFound("assetTag.specified=true");

        // Get all the assetList where assetTag is null
        defaultAssetShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseCostIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseCost equals to DEFAULT_PURCHASE_COST
        defaultAssetShouldBeFound("purchaseCost.equals=" + DEFAULT_PURCHASE_COST);

        // Get all the assetList where purchaseCost equals to UPDATED_PURCHASE_COST
        defaultAssetShouldNotBeFound("purchaseCost.equals=" + UPDATED_PURCHASE_COST);
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseCostIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseCost in DEFAULT_PURCHASE_COST or UPDATED_PURCHASE_COST
        defaultAssetShouldBeFound("purchaseCost.in=" + DEFAULT_PURCHASE_COST + "," + UPDATED_PURCHASE_COST);

        // Get all the assetList where purchaseCost equals to UPDATED_PURCHASE_COST
        defaultAssetShouldNotBeFound("purchaseCost.in=" + UPDATED_PURCHASE_COST);
    }

    @Test
    @Transactional
    public void getAllAssetsByPurchaseCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where purchaseCost is not null
        defaultAssetShouldBeFound("purchaseCost.specified=true");

        // Get all the assetList where purchaseCost is null
        defaultAssetShouldNotBeFound("purchaseCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        asset.setCategory(category);
        assetRepository.saveAndFlush(asset);
        Long categoryId = category.getId();

        // Get all the assetList where category equals to categoryId
        defaultAssetShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the assetList where category equals to categoryId + 1
        defaultAssetShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllAssetsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        ServiceOutlet serviceOutlet = ServiceOutletResourceIntTest.createEntity(em);
        em.persist(serviceOutlet);
        em.flush();
        asset.setServiceOutlet(serviceOutlet);
        assetRepository.saveAndFlush(asset);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the assetList where serviceOutlet equals to serviceOutletId
        defaultAssetShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the assetList where serviceOutlet equals to serviceOutletId + 1
        defaultAssetShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAssetShouldBeFound(String filter) throws Exception {
        restAssetMockMvc.perform(get("/api/assets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].purchaseCost").value(hasItem(DEFAULT_PURCHASE_COST.intValue())));

        // Check, that the count call also returns 1
        restAssetMockMvc.perform(get("/api/assets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAssetShouldNotBeFound(String filter) throws Exception {
        restAssetMockMvc.perform(get("/api/assets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetMockMvc.perform(get("/api/assets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAsset() throws Exception {
        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset
        Asset updatedAsset = assetRepository.findById(asset.getId()).get();
        // Disconnect from session so that the updates on updatedAsset are not directly saved in db
        em.detach(updatedAsset);
        updatedAsset
            .description(UPDATED_DESCRIPTION)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .assetTag(UPDATED_ASSET_TAG)
            .purchaseCost(UPDATED_PURCHASE_COST);
        AssetDTO assetDTO = assetMapper.toDto(updatedAsset);

        restAssetMockMvc.perform(put("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAsset.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testAsset.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAsset.getPurchaseCost()).isEqualTo(UPDATED_PURCHASE_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc.perform(put("/api/assets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeDelete = assetRepository.findAll().size();

        // Delete the asset
        restAssetMockMvc.perform(delete("/api/assets/{id}", asset.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Asset.class);
        Asset asset1 = new Asset();
        asset1.setId(1L);
        Asset asset2 = new Asset();
        asset2.setId(asset1.getId());
        assertThat(asset1).isEqualTo(asset2);
        asset2.setId(2L);
        assertThat(asset1).isNotEqualTo(asset2);
        asset1.setId(null);
        assertThat(asset1).isNotEqualTo(asset2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDTO.class);
        AssetDTO assetDTO1 = new AssetDTO();
        assetDTO1.setId(1L);
        AssetDTO assetDTO2 = new AssetDTO();
        assertThat(assetDTO1).isNotEqualTo(assetDTO2);
        assetDTO2.setId(assetDTO1.getId());
        assertThat(assetDTO1).isEqualTo(assetDTO2);
        assetDTO2.setId(2L);
        assertThat(assetDTO1).isNotEqualTo(assetDTO2);
        assetDTO1.setId(null);
        assertThat(assetDTO1).isNotEqualTo(assetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(assetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(assetMapper.fromId(null)).isNull();
    }
}
