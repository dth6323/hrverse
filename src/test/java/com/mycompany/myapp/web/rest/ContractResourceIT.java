package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ContractAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Contract;
import com.mycompany.myapp.repository.ContractRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    private Contract insertedContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity() {
        return new Contract()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .contractCode(DEFAULT_CONTRACT_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity() {
        return new Contract()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .contractCode(UPDATED_CONTRACT_CODE);
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedContract != null) {
            contractRepository.delete(insertedContract);
            insertedContract = null;
        }
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contract
        var returnedContract = om.readValue(
            restContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Contract.class
        );

        // Validate the Contract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContractUpdatableFieldsEquals(returnedContract, getPersistedContract(returnedContract));

        insertedContract = returnedContract;
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setStartDate(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setEndDate(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setStatus(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setContractCode(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].contractCode").value(hasItem(DEFAULT_CONTRACT_CODE)));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.contractCode").value(DEFAULT_CONTRACT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).status(UPDATED_STATUS).contractCode(UPDATED_CONTRACT_CODE);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractToMatchAllProperties(updatedContract);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contract.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract.status(UPDATED_STATUS);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContract, contract), getPersistedContract(contract));
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .contractCode(UPDATED_CONTRACT_CODE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(partialUpdatedContract, getPersistedContract(partialUpdatedContract));
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Contract getPersistedContract(Contract contract) {
        return contractRepository.findById(contract.getId()).orElseThrow();
    }

    protected void assertPersistedContractToMatchAllProperties(Contract expectedContract) {
        assertContractAllPropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }

    protected void assertPersistedContractToMatchUpdatableProperties(Contract expectedContract) {
        assertContractAllUpdatablePropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }
}
