package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ContractTerminationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ContractTermination;
import com.mycompany.myapp.repository.ContractTerminationRepository;
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
 * Integration tests for the {@link ContractTerminationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractTerminationResourceIT {

    private static final LocalDate DEFAULT_TERMINATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Float DEFAULT_COMPENSATION = 1F;
    private static final Float UPDATED_COMPENSATION = 2F;

    private static final String ENTITY_API_URL = "/api/contract-terminations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractTerminationRepository contractTerminationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractTerminationMockMvc;

    private ContractTermination contractTermination;

    private ContractTermination insertedContractTermination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractTermination createEntity() {
        return new ContractTermination()
            .terminationDate(DEFAULT_TERMINATION_DATE)
            .reason(DEFAULT_REASON)
            .compensation(DEFAULT_COMPENSATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractTermination createUpdatedEntity() {
        return new ContractTermination()
            .terminationDate(UPDATED_TERMINATION_DATE)
            .reason(UPDATED_REASON)
            .compensation(UPDATED_COMPENSATION);
    }

    @BeforeEach
    public void initTest() {
        contractTermination = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedContractTermination != null) {
            contractTerminationRepository.delete(insertedContractTermination);
            insertedContractTermination = null;
        }
    }

    @Test
    @Transactional
    void createContractTermination() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContractTermination
        var returnedContractTermination = om.readValue(
            restContractTerminationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractTermination)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractTermination.class
        );

        // Validate the ContractTermination in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContractTerminationUpdatableFieldsEquals(
            returnedContractTermination,
            getPersistedContractTermination(returnedContractTermination)
        );

        insertedContractTermination = returnedContractTermination;
    }

    @Test
    @Transactional
    void createContractTerminationWithExistingId() throws Exception {
        // Create the ContractTermination with an existing ID
        contractTermination.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractTerminationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractTermination)))
            .andExpect(status().isBadRequest());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTerminationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractTermination.setTerminationDate(null);

        // Create the ContractTermination, which fails.

        restContractTerminationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractTermination)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractTermination.setReason(null);

        // Create the ContractTermination, which fails.

        restContractTerminationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractTermination)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractTerminations() throws Exception {
        // Initialize the database
        insertedContractTermination = contractTerminationRepository.saveAndFlush(contractTermination);

        // Get all the contractTerminationList
        restContractTerminationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractTermination.getId().intValue())))
            .andExpect(jsonPath("$.[*].terminationDate").value(hasItem(DEFAULT_TERMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].compensation").value(hasItem(DEFAULT_COMPENSATION.doubleValue())));
    }

    @Test
    @Transactional
    void getContractTermination() throws Exception {
        // Initialize the database
        insertedContractTermination = contractTerminationRepository.saveAndFlush(contractTermination);

        // Get the contractTermination
        restContractTerminationMockMvc
            .perform(get(ENTITY_API_URL_ID, contractTermination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractTermination.getId().intValue()))
            .andExpect(jsonPath("$.terminationDate").value(DEFAULT_TERMINATION_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.compensation").value(DEFAULT_COMPENSATION.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingContractTermination() throws Exception {
        // Get the contractTermination
        restContractTerminationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContractTermination() throws Exception {
        // Initialize the database
        insertedContractTermination = contractTerminationRepository.saveAndFlush(contractTermination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractTermination
        ContractTermination updatedContractTermination = contractTerminationRepository.findById(contractTermination.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContractTermination are not directly saved in db
        em.detach(updatedContractTermination);
        updatedContractTermination.terminationDate(UPDATED_TERMINATION_DATE).reason(UPDATED_REASON).compensation(UPDATED_COMPENSATION);

        restContractTerminationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContractTermination.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContractTermination))
            )
            .andExpect(status().isOk());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractTerminationToMatchAllProperties(updatedContractTermination);
    }

    @Test
    @Transactional
    void putNonExistingContractTermination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractTermination.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractTerminationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractTermination.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractTermination))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractTermination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractTermination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTerminationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractTermination))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractTermination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractTermination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTerminationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractTermination)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractTerminationWithPatch() throws Exception {
        // Initialize the database
        insertedContractTermination = contractTerminationRepository.saveAndFlush(contractTermination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractTermination using partial update
        ContractTermination partialUpdatedContractTermination = new ContractTermination();
        partialUpdatedContractTermination.setId(contractTermination.getId());

        restContractTerminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractTermination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractTermination))
            )
            .andExpect(status().isOk());

        // Validate the ContractTermination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractTerminationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContractTermination, contractTermination),
            getPersistedContractTermination(contractTermination)
        );
    }

    @Test
    @Transactional
    void fullUpdateContractTerminationWithPatch() throws Exception {
        // Initialize the database
        insertedContractTermination = contractTerminationRepository.saveAndFlush(contractTermination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractTermination using partial update
        ContractTermination partialUpdatedContractTermination = new ContractTermination();
        partialUpdatedContractTermination.setId(contractTermination.getId());

        partialUpdatedContractTermination
            .terminationDate(UPDATED_TERMINATION_DATE)
            .reason(UPDATED_REASON)
            .compensation(UPDATED_COMPENSATION);

        restContractTerminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractTermination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractTermination))
            )
            .andExpect(status().isOk());

        // Validate the ContractTermination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractTerminationUpdatableFieldsEquals(
            partialUpdatedContractTermination,
            getPersistedContractTermination(partialUpdatedContractTermination)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContractTermination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractTermination.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractTerminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractTermination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractTermination))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractTermination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractTermination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTerminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractTermination))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractTermination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractTermination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTerminationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractTermination)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractTermination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractTermination() throws Exception {
        // Initialize the database
        insertedContractTermination = contractTerminationRepository.saveAndFlush(contractTermination);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contractTermination
        restContractTerminationMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractTermination.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractTerminationRepository.count();
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

    protected ContractTermination getPersistedContractTermination(ContractTermination contractTermination) {
        return contractTerminationRepository.findById(contractTermination.getId()).orElseThrow();
    }

    protected void assertPersistedContractTerminationToMatchAllProperties(ContractTermination expectedContractTermination) {
        assertContractTerminationAllPropertiesEquals(
            expectedContractTermination,
            getPersistedContractTermination(expectedContractTermination)
        );
    }

    protected void assertPersistedContractTerminationToMatchUpdatableProperties(ContractTermination expectedContractTermination) {
        assertContractTerminationAllUpdatablePropertiesEquals(
            expectedContractTermination,
            getPersistedContractTermination(expectedContractTermination)
        );
    }
}
