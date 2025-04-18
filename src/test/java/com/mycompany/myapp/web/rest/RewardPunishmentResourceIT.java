package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.RewardPunishmentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RewardPunishment;
import com.mycompany.myapp.repository.RewardPunishmentRepository;
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
 * Integration tests for the {@link RewardPunishmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RewardPunishmentResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_APPLY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reward-punishments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RewardPunishmentRepository rewardPunishmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRewardPunishmentMockMvc;

    private RewardPunishment rewardPunishment;

    private RewardPunishment insertedRewardPunishment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RewardPunishment createEntity() {
        return new RewardPunishment()
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .reason(DEFAULT_REASON)
            .applyDate(DEFAULT_APPLY_DATE)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RewardPunishment createUpdatedEntity() {
        return new RewardPunishment()
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .reason(UPDATED_REASON)
            .applyDate(UPDATED_APPLY_DATE)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    public void initTest() {
        rewardPunishment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRewardPunishment != null) {
            rewardPunishmentRepository.delete(insertedRewardPunishment);
            insertedRewardPunishment = null;
        }
    }

    @Test
    @Transactional
    void createRewardPunishment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RewardPunishment
        var returnedRewardPunishment = om.readValue(
            restRewardPunishmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardPunishment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RewardPunishment.class
        );

        // Validate the RewardPunishment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRewardPunishmentUpdatableFieldsEquals(returnedRewardPunishment, getPersistedRewardPunishment(returnedRewardPunishment));

        insertedRewardPunishment = returnedRewardPunishment;
    }

    @Test
    @Transactional
    void createRewardPunishmentWithExistingId() throws Exception {
        // Create the RewardPunishment with an existing ID
        rewardPunishment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRewardPunishmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardPunishment)))
            .andExpect(status().isBadRequest());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rewardPunishment.setType(null);

        // Create the RewardPunishment, which fails.

        restRewardPunishmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardPunishment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rewardPunishment.setAmount(null);

        // Create the RewardPunishment, which fails.

        restRewardPunishmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardPunishment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rewardPunishment.setReason(null);

        // Create the RewardPunishment, which fails.

        restRewardPunishmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardPunishment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApplyDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rewardPunishment.setApplyDate(null);

        // Create the RewardPunishment, which fails.

        restRewardPunishmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardPunishment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRewardPunishments() throws Exception {
        // Initialize the database
        insertedRewardPunishment = rewardPunishmentRepository.saveAndFlush(rewardPunishment);

        // Get all the rewardPunishmentList
        restRewardPunishmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rewardPunishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].applyDate").value(hasItem(DEFAULT_APPLY_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getRewardPunishment() throws Exception {
        // Initialize the database
        insertedRewardPunishment = rewardPunishmentRepository.saveAndFlush(rewardPunishment);

        // Get the rewardPunishment
        restRewardPunishmentMockMvc
            .perform(get(ENTITY_API_URL_ID, rewardPunishment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rewardPunishment.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.applyDate").value(DEFAULT_APPLY_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingRewardPunishment() throws Exception {
        // Get the rewardPunishment
        restRewardPunishmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRewardPunishment() throws Exception {
        // Initialize the database
        insertedRewardPunishment = rewardPunishmentRepository.saveAndFlush(rewardPunishment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rewardPunishment
        RewardPunishment updatedRewardPunishment = rewardPunishmentRepository.findById(rewardPunishment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRewardPunishment are not directly saved in db
        em.detach(updatedRewardPunishment);
        updatedRewardPunishment
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .reason(UPDATED_REASON)
            .applyDate(UPDATED_APPLY_DATE)
            .notes(UPDATED_NOTES);

        restRewardPunishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRewardPunishment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRewardPunishment))
            )
            .andExpect(status().isOk());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRewardPunishmentToMatchAllProperties(updatedRewardPunishment);
    }

    @Test
    @Transactional
    void putNonExistingRewardPunishment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardPunishment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardPunishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rewardPunishment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rewardPunishment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRewardPunishment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardPunishment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardPunishmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rewardPunishment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRewardPunishment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardPunishment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardPunishmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardPunishment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRewardPunishmentWithPatch() throws Exception {
        // Initialize the database
        insertedRewardPunishment = rewardPunishmentRepository.saveAndFlush(rewardPunishment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rewardPunishment using partial update
        RewardPunishment partialUpdatedRewardPunishment = new RewardPunishment();
        partialUpdatedRewardPunishment.setId(rewardPunishment.getId());

        partialUpdatedRewardPunishment.applyDate(UPDATED_APPLY_DATE);

        restRewardPunishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRewardPunishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRewardPunishment))
            )
            .andExpect(status().isOk());

        // Validate the RewardPunishment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRewardPunishmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRewardPunishment, rewardPunishment),
            getPersistedRewardPunishment(rewardPunishment)
        );
    }

    @Test
    @Transactional
    void fullUpdateRewardPunishmentWithPatch() throws Exception {
        // Initialize the database
        insertedRewardPunishment = rewardPunishmentRepository.saveAndFlush(rewardPunishment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rewardPunishment using partial update
        RewardPunishment partialUpdatedRewardPunishment = new RewardPunishment();
        partialUpdatedRewardPunishment.setId(rewardPunishment.getId());

        partialUpdatedRewardPunishment
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .reason(UPDATED_REASON)
            .applyDate(UPDATED_APPLY_DATE)
            .notes(UPDATED_NOTES);

        restRewardPunishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRewardPunishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRewardPunishment))
            )
            .andExpect(status().isOk());

        // Validate the RewardPunishment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRewardPunishmentUpdatableFieldsEquals(
            partialUpdatedRewardPunishment,
            getPersistedRewardPunishment(partialUpdatedRewardPunishment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRewardPunishment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardPunishment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardPunishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rewardPunishment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rewardPunishment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRewardPunishment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardPunishment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardPunishmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rewardPunishment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRewardPunishment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardPunishment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardPunishmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rewardPunishment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RewardPunishment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRewardPunishment() throws Exception {
        // Initialize the database
        insertedRewardPunishment = rewardPunishmentRepository.saveAndFlush(rewardPunishment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rewardPunishment
        restRewardPunishmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, rewardPunishment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rewardPunishmentRepository.count();
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

    protected RewardPunishment getPersistedRewardPunishment(RewardPunishment rewardPunishment) {
        return rewardPunishmentRepository.findById(rewardPunishment.getId()).orElseThrow();
    }

    protected void assertPersistedRewardPunishmentToMatchAllProperties(RewardPunishment expectedRewardPunishment) {
        assertRewardPunishmentAllPropertiesEquals(expectedRewardPunishment, getPersistedRewardPunishment(expectedRewardPunishment));
    }

    protected void assertPersistedRewardPunishmentToMatchUpdatableProperties(RewardPunishment expectedRewardPunishment) {
        assertRewardPunishmentAllUpdatablePropertiesEquals(
            expectedRewardPunishment,
            getPersistedRewardPunishment(expectedRewardPunishment)
        );
    }
}
