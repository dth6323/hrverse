package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.WageAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Wage;
import com.mycompany.myapp.repository.WageRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link WageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WageResourceIT {

    private static final Float DEFAULT_COEFFICIENTS = 1F;
    private static final Float UPDATED_COEFFICIENTS = 2F;

    private static final Float DEFAULT_BASE_SALARY = 1F;
    private static final Float UPDATED_BASE_SALARY = 2F;

    private static final Float DEFAULT_ALLOWANCE = 1F;
    private static final Float UPDATED_ALLOWANCE = 2F;

    private static final String ENTITY_API_URL = "/api/wages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WageRepository wageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWageMockMvc;

    private Wage wage;

    private Wage insertedWage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wage createEntity() {
        return new Wage().coefficients(DEFAULT_COEFFICIENTS).baseSalary(DEFAULT_BASE_SALARY).allowance(DEFAULT_ALLOWANCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wage createUpdatedEntity() {
        return new Wage().coefficients(UPDATED_COEFFICIENTS).baseSalary(UPDATED_BASE_SALARY).allowance(UPDATED_ALLOWANCE);
    }

    @BeforeEach
    public void initTest() {
        wage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWage != null) {
            wageRepository.delete(insertedWage);
            insertedWage = null;
        }
    }

    @Test
    @Transactional
    void createWage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Wage
        var returnedWage = om.readValue(
            restWageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wage)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Wage.class
        );

        // Validate the Wage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWageUpdatableFieldsEquals(returnedWage, getPersistedWage(returnedWage));

        insertedWage = returnedWage;
    }

    @Test
    @Transactional
    void createWageWithExistingId() throws Exception {
        // Create the Wage with an existing ID
        wage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wage)))
            .andExpect(status().isBadRequest());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCoefficientsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wage.setCoefficients(null);

        // Create the Wage, which fails.

        restWageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBaseSalaryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wage.setBaseSalary(null);

        // Create the Wage, which fails.

        restWageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllowanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wage.setAllowance(null);

        // Create the Wage, which fails.

        restWageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWages() throws Exception {
        // Initialize the database
        insertedWage = wageRepository.saveAndFlush(wage);

        // Get all the wageList
        restWageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wage.getId().intValue())))
            .andExpect(jsonPath("$.[*].coefficients").value(hasItem(DEFAULT_COEFFICIENTS.doubleValue())))
            .andExpect(jsonPath("$.[*].baseSalary").value(hasItem(DEFAULT_BASE_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance").value(hasItem(DEFAULT_ALLOWANCE.doubleValue())));
    }

    @Test
    @Transactional
    void getWage() throws Exception {
        // Initialize the database
        insertedWage = wageRepository.saveAndFlush(wage);

        // Get the wage
        restWageMockMvc
            .perform(get(ENTITY_API_URL_ID, wage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wage.getId().intValue()))
            .andExpect(jsonPath("$.coefficients").value(DEFAULT_COEFFICIENTS.doubleValue()))
            .andExpect(jsonPath("$.baseSalary").value(DEFAULT_BASE_SALARY.doubleValue()))
            .andExpect(jsonPath("$.allowance").value(DEFAULT_ALLOWANCE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingWage() throws Exception {
        // Get the wage
        restWageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWage() throws Exception {
        // Initialize the database
        insertedWage = wageRepository.saveAndFlush(wage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wage
        Wage updatedWage = wageRepository.findById(wage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWage are not directly saved in db
        em.detach(updatedWage);
        updatedWage.coefficients(UPDATED_COEFFICIENTS).baseSalary(UPDATED_BASE_SALARY).allowance(UPDATED_ALLOWANCE);

        restWageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWage))
            )
            .andExpect(status().isOk());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWageToMatchAllProperties(updatedWage);
    }

    @Test
    @Transactional
    void putNonExistingWage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWageMockMvc
            .perform(put(ENTITY_API_URL_ID, wage.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wage)))
            .andExpect(status().isBadRequest());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWageWithPatch() throws Exception {
        // Initialize the database
        insertedWage = wageRepository.saveAndFlush(wage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wage using partial update
        Wage partialUpdatedWage = new Wage();
        partialUpdatedWage.setId(wage.getId());

        partialUpdatedWage.coefficients(UPDATED_COEFFICIENTS);

        restWageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWage))
            )
            .andExpect(status().isOk());

        // Validate the Wage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWage, wage), getPersistedWage(wage));
    }

    @Test
    @Transactional
    void fullUpdateWageWithPatch() throws Exception {
        // Initialize the database
        insertedWage = wageRepository.saveAndFlush(wage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wage using partial update
        Wage partialUpdatedWage = new Wage();
        partialUpdatedWage.setId(wage.getId());

        partialUpdatedWage.coefficients(UPDATED_COEFFICIENTS).baseSalary(UPDATED_BASE_SALARY).allowance(UPDATED_ALLOWANCE);

        restWageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWage))
            )
            .andExpect(status().isOk());

        // Validate the Wage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWageUpdatableFieldsEquals(partialUpdatedWage, getPersistedWage(partialUpdatedWage));
    }

    @Test
    @Transactional
    void patchNonExistingWage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWageMockMvc
            .perform(patch(ENTITY_API_URL_ID, wage.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wage)))
            .andExpect(status().isBadRequest());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWage() throws Exception {
        // Initialize the database
        insertedWage = wageRepository.saveAndFlush(wage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the wage
        restWageMockMvc
            .perform(delete(ENTITY_API_URL_ID, wage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return wageRepository.count();
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

    protected Wage getPersistedWage(Wage wage) {
        return wageRepository.findById(wage.getId()).orElseThrow();
    }

    protected void assertPersistedWageToMatchAllProperties(Wage expectedWage) {
        assertWageAllPropertiesEquals(expectedWage, getPersistedWage(expectedWage));
    }

    protected void assertPersistedWageToMatchUpdatableProperties(Wage expectedWage) {
        assertWageAllUpdatablePropertiesEquals(expectedWage, getPersistedWage(expectedWage));
    }
}
