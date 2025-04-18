package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ResignationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Resignation;
import com.mycompany.myapp.repository.ResignationRepository;
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
 * Integration tests for the {@link ResignationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResignationResourceIT {

    private static final LocalDate DEFAULT_SUBMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBMISSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resignations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResignationRepository resignationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResignationMockMvc;

    private Resignation resignation;

    private Resignation insertedResignation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resignation createEntity() {
        return new Resignation()
            .submissionDate(DEFAULT_SUBMISSION_DATE)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .reason(DEFAULT_REASON)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resignation createUpdatedEntity() {
        return new Resignation()
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    public void initTest() {
        resignation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedResignation != null) {
            resignationRepository.delete(insertedResignation);
            insertedResignation = null;
        }
    }

    @Test
    @Transactional
    void createResignation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Resignation
        var returnedResignation = om.readValue(
            restResignationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resignation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Resignation.class
        );

        // Validate the Resignation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertResignationUpdatableFieldsEquals(returnedResignation, getPersistedResignation(returnedResignation));

        insertedResignation = returnedResignation;
    }

    @Test
    @Transactional
    void createResignationWithExistingId() throws Exception {
        // Create the Resignation with an existing ID
        resignation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resignation)))
            .andExpect(status().isBadRequest());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubmissionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resignation.setSubmissionDate(null);

        // Create the Resignation, which fails.

        restResignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resignation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEffectiveDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resignation.setEffectiveDate(null);

        // Create the Resignation, which fails.

        restResignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resignation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resignation.setReason(null);

        // Create the Resignation, which fails.

        restResignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resignation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resignation.setStatus(null);

        // Create the Resignation, which fails.

        restResignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resignation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResignations() throws Exception {
        // Initialize the database
        insertedResignation = resignationRepository.saveAndFlush(resignation);

        // Get all the resignationList
        restResignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resignation.getId().intValue())))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getResignation() throws Exception {
        // Initialize the database
        insertedResignation = resignationRepository.saveAndFlush(resignation);

        // Get the resignation
        restResignationMockMvc
            .perform(get(ENTITY_API_URL_ID, resignation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resignation.getId().intValue()))
            .andExpect(jsonPath("$.submissionDate").value(DEFAULT_SUBMISSION_DATE.toString()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingResignation() throws Exception {
        // Get the resignation
        restResignationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResignation() throws Exception {
        // Initialize the database
        insertedResignation = resignationRepository.saveAndFlush(resignation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resignation
        Resignation updatedResignation = resignationRepository.findById(resignation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResignation are not directly saved in db
        em.detach(updatedResignation);
        updatedResignation
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restResignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResignation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedResignation))
            )
            .andExpect(status().isOk());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResignationToMatchAllProperties(updatedResignation);
    }

    @Test
    @Transactional
    void putNonExistingResignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resignation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resignation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resignation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resignation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resignation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resignation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResignationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resignation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResignationWithPatch() throws Exception {
        // Initialize the database
        insertedResignation = resignationRepository.saveAndFlush(resignation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resignation using partial update
        Resignation partialUpdatedResignation = new Resignation();
        partialUpdatedResignation.setId(resignation.getId());

        partialUpdatedResignation.submissionDate(UPDATED_SUBMISSION_DATE).reason(UPDATED_REASON).status(UPDATED_STATUS);

        restResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResignation))
            )
            .andExpect(status().isOk());

        // Validate the Resignation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResignationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResignation, resignation),
            getPersistedResignation(resignation)
        );
    }

    @Test
    @Transactional
    void fullUpdateResignationWithPatch() throws Exception {
        // Initialize the database
        insertedResignation = resignationRepository.saveAndFlush(resignation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resignation using partial update
        Resignation partialUpdatedResignation = new Resignation();
        partialUpdatedResignation.setId(resignation.getId());

        partialUpdatedResignation
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResignation))
            )
            .andExpect(status().isOk());

        // Validate the Resignation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResignationUpdatableFieldsEquals(partialUpdatedResignation, getPersistedResignation(partialUpdatedResignation));
    }

    @Test
    @Transactional
    void patchNonExistingResignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resignation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resignation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resignation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resignation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resignation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResignationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resignation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resignation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResignation() throws Exception {
        // Initialize the database
        insertedResignation = resignationRepository.saveAndFlush(resignation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resignation
        restResignationMockMvc
            .perform(delete(ENTITY_API_URL_ID, resignation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resignationRepository.count();
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

    protected Resignation getPersistedResignation(Resignation resignation) {
        return resignationRepository.findById(resignation.getId()).orElseThrow();
    }

    protected void assertPersistedResignationToMatchAllProperties(Resignation expectedResignation) {
        assertResignationAllPropertiesEquals(expectedResignation, getPersistedResignation(expectedResignation));
    }

    protected void assertPersistedResignationToMatchUpdatableProperties(Resignation expectedResignation) {
        assertResignationAllUpdatablePropertiesEquals(expectedResignation, getPersistedResignation(expectedResignation));
    }
}
