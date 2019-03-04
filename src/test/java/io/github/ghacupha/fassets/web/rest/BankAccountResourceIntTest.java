package io.github.ghacupha.fassets.web.rest;

import io.github.ghacupha.fassets.FassetsApp;

import io.github.ghacupha.fassets.domain.BankAccount;
import io.github.ghacupha.fassets.domain.Category;
import io.github.ghacupha.fassets.repository.BankAccountRepository;
import io.github.ghacupha.fassets.service.BankAccountService;
import io.github.ghacupha.fassets.service.dto.BankAccountDTO;
import io.github.ghacupha.fassets.service.mapper.BankAccountMapper;
import io.github.ghacupha.fassets.web.rest.errors.ExceptionTranslator;
import io.github.ghacupha.fassets.service.dto.BankAccountCriteria;
import io.github.ghacupha.fassets.service.BankAccountQueryService;

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
import java.util.List;


import static io.github.ghacupha.fassets.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BankAccountResource REST controller.
 *
 * @see BankAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FassetsApp.class)
public class BankAccountResourceIntTest {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE = new BigDecimal(2);

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountQueryService bankAccountQueryService;

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

    private MockMvc restBankAccountMockMvc;

    private BankAccount bankAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BankAccountResource bankAccountResource = new BankAccountResource(bankAccountService, bankAccountQueryService);
        this.restBankAccountMockMvc = MockMvcBuilders.standaloneSetup(bankAccountResource)
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
    public static BankAccount createEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .accountBalance(DEFAULT_ACCOUNT_BALANCE);
        return bankAccount;
    }

    @Before
    public void initTest() {
        bankAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createBankAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);
        restBankAccountMockMvc.perform(post("/api/bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testBankAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testBankAccount.getAccountBalance()).isEqualTo(DEFAULT_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void createBankAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // Create the BankAccount with an existing ID
        bankAccount.setId(1L);
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankAccountMockMvc.perform(post("/api/bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setAccountName(null);

        // Create the BankAccount, which fails.
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        restBankAccountMockMvc.perform(post("/api/bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setAccountNumber(null);

        // Create the BankAccount, which fails.
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        restBankAccountMockMvc.perform(post("/api/bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setAccountBalance(null);

        // Create the BankAccount, which fails.
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        restBankAccountMockMvc.perform(post("/api/bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBankAccounts() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", bankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.accountBalance").value(DEFAULT_ACCOUNT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultBankAccountShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the bankAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultBankAccountShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultBankAccountShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the bankAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultBankAccountShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountName is not null
        defaultBankAccountShouldBeFound("accountName.specified=true");

        // Get all the bankAccountList where accountName is null
        defaultBankAccountShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultBankAccountShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the bankAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the bankAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber is not null
        defaultBankAccountShouldBeFound("accountNumber.specified=true");

        // Get all the bankAccountList where accountNumber is null
        defaultBankAccountShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountBalance equals to DEFAULT_ACCOUNT_BALANCE
        defaultBankAccountShouldBeFound("accountBalance.equals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the bankAccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultBankAccountShouldNotBeFound("accountBalance.equals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountBalance in DEFAULT_ACCOUNT_BALANCE or UPDATED_ACCOUNT_BALANCE
        defaultBankAccountShouldBeFound("accountBalance.in=" + DEFAULT_ACCOUNT_BALANCE + "," + UPDATED_ACCOUNT_BALANCE);

        // Get all the bankAccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultBankAccountShouldNotBeFound("accountBalance.in=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByAccountBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountBalance is not null
        defaultBankAccountShouldBeFound("accountBalance.specified=true");

        // Get all the bankAccountList where accountBalance is null
        defaultBankAccountShouldNotBeFound("accountBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankAccountsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        bankAccount.setCategory(category);
        category.setBankAccount(bankAccount);
        bankAccountRepository.saveAndFlush(bankAccount);
        Long categoryId = category.getId();

        // Get all the bankAccountList where category equals to categoryId
        defaultBankAccountShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the bankAccountList where category equals to categoryId + 1
        defaultBankAccountShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBankAccountShouldBeFound(String filter) throws Exception {
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restBankAccountMockMvc.perform(get("/api/bank-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBankAccountShouldNotBeFound(String filter) throws Exception {
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankAccountMockMvc.perform(get("/api/bank-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount
        BankAccount updatedBankAccount = bankAccountRepository.findById(bankAccount.getId()).get();
        // Disconnect from session so that the updates on updatedBankAccount are not directly saved in db
        em.detach(updatedBankAccount);
        updatedBankAccount
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountBalance(UPDATED_ACCOUNT_BALANCE);
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(updatedBankAccount);

        restBankAccountMockMvc.perform(put("/api/bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testBankAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testBankAccount.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc.perform(put("/api/bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeDelete = bankAccountRepository.findAll().size();

        // Delete the bankAccount
        restBankAccountMockMvc.perform(delete("/api/bank-accounts/{id}", bankAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankAccount.class);
        BankAccount bankAccount1 = new BankAccount();
        bankAccount1.setId(1L);
        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setId(bankAccount1.getId());
        assertThat(bankAccount1).isEqualTo(bankAccount2);
        bankAccount2.setId(2L);
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);
        bankAccount1.setId(null);
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankAccountDTO.class);
        BankAccountDTO bankAccountDTO1 = new BankAccountDTO();
        bankAccountDTO1.setId(1L);
        BankAccountDTO bankAccountDTO2 = new BankAccountDTO();
        assertThat(bankAccountDTO1).isNotEqualTo(bankAccountDTO2);
        bankAccountDTO2.setId(bankAccountDTO1.getId());
        assertThat(bankAccountDTO1).isEqualTo(bankAccountDTO2);
        bankAccountDTO2.setId(2L);
        assertThat(bankAccountDTO1).isNotEqualTo(bankAccountDTO2);
        bankAccountDTO1.setId(null);
        assertThat(bankAccountDTO1).isNotEqualTo(bankAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bankAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bankAccountMapper.fromId(null)).isNull();
    }
}
