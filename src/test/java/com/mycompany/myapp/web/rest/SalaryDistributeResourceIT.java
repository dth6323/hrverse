package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SalaryDistributeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SalaryDistribute;
import com.mycompany.myapp.repository.SalaryDistributeRepository;
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
 * Integration tests for the {@link SalaryDistributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalaryDistributeResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_WORK_DAY = 1;
    private static final Integer UPDATED_WORK_DAY = 2;

    private static final String DEFAULT_TYPE_OF_SALARY = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_OF_SALARY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/salary-distributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SalaryDistributeRepository salaryDistributeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaryDistributeMockMvc;

    private SalaryDistribute salaryDistribute;

    private SalaryDistribute insertedSalaryDistribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryDistribute createEntity() {
        return new SalaryDistribute()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .workDay(DEFAULT_WORK_DAY)
            .typeOfSalary(DEFAULT_TYPE_OF_SALARY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryDistribute createUpdatedEntity() {
        return new SalaryDistribute()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .workDay(UPDATED_WORK_DAY)
            .typeOfSalary(UPDATED_TYPE_OF_SALARY);
    }

    @BeforeEach
    public void initTest() {
        salaryDistribute = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSalaryDistribute != null) {
            salaryDistributeRepository.delete(insertedSalaryDistribute);
            insertedSalaryDistribute = null;
        }
    }

    @Test
    @Transactional
    void createSalaryDistribute() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SalaryDistribute
        var returnedSalaryDistribute = om.readValue(
            restSalaryDistributeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryDistribute)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SalaryDistribute.class
        );

        // Validate the SalaryDistribute in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSalaryDistributeUpdatableFieldsEquals(returnedSalaryDistribute, getPersistedSalaryDistribute(returnedSalaryDistribute));

        insertedSalaryDistribute = returnedSalaryDistribute;
    }

    @Test
    @Transactional
    void createSalaryDistributeWithExistingId() throws Exception {
        // Create the SalaryDistribute with an existing ID
        salaryDistribute.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryDistributeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryDistribute)))
            .andExpect(status().isBadRequest());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        salaryDistribute.setStartDate(null);

        // Create the SalaryDistribute, which fails.

        restSalaryDistributeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryDistribute)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        salaryDistribute.setEndDate(null);

        // Create the SalaryDistribute, which fails.

        restSalaryDistributeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryDistribute)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkDayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        salaryDistribute.setWorkDay(null);

        // Create the SalaryDistribute, which fails.

        restSalaryDistributeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryDistribute)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeOfSalaryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        salaryDistribute.setTypeOfSalary(null);

        // Create the SalaryDistribute, which fails.

        restSalaryDistributeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryDistribute)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalaryDistributes() throws Exception {
        // Initialize the database
        insertedSalaryDistribute = salaryDistributeRepository.saveAndFlush(salaryDistribute);

        // Get all the salaryDistributeList
        restSalaryDistributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryDistribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].workDay").value(hasItem(DEFAULT_WORK_DAY)))
            .andExpect(jsonPath("$.[*].typeOfSalary").value(hasItem(DEFAULT_TYPE_OF_SALARY)));
    }

    @Test
    @Transactional
    void getSalaryDistribute() throws Exception {
        // Initialize the database
        insertedSalaryDistribute = salaryDistributeRepository.saveAndFlush(salaryDistribute);

        // Get the salaryDistribute
        restSalaryDistributeMockMvc
            .perform(get(ENTITY_API_URL_ID, salaryDistribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salaryDistribute.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.workDay").value(DEFAULT_WORK_DAY))
            .andExpect(jsonPath("$.typeOfSalary").value(DEFAULT_TYPE_OF_SALARY));
    }

    @Test
    @Transactional
    void getNonExistingSalaryDistribute() throws Exception {
        // Get the salaryDistribute
        restSalaryDistributeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalaryDistribute() throws Exception {
        // Initialize the database
        insertedSalaryDistribute = salaryDistributeRepository.saveAndFlush(salaryDistribute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salaryDistribute
        SalaryDistribute updatedSalaryDistribute = salaryDistributeRepository.findById(salaryDistribute.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSalaryDistribute are not directly saved in db
        em.detach(updatedSalaryDistribute);
        updatedSalaryDistribute
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .workDay(UPDATED_WORK_DAY)
            .typeOfSalary(UPDATED_TYPE_OF_SALARY);

        restSalaryDistributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSalaryDistribute.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSalaryDistribute))
            )
            .andExpect(status().isOk());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSalaryDistributeToMatchAllProperties(updatedSalaryDistribute);
    }

    @Test
    @Transactional
    void putNonExistingSalaryDistribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salaryDistribute.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryDistributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryDistribute.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salaryDistribute))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalaryDistribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salaryDistribute.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDistributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salaryDistribute))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalaryDistribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salaryDistribute.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDistributeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salaryDistribute)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalaryDistributeWithPatch() throws Exception {
        // Initialize the database
        insertedSalaryDistribute = salaryDistributeRepository.saveAndFlush(salaryDistribute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salaryDistribute using partial update
        SalaryDistribute partialUpdatedSalaryDistribute = new SalaryDistribute();
        partialUpdatedSalaryDistribute.setId(salaryDistribute.getId());

        partialUpdatedSalaryDistribute.endDate(UPDATED_END_DATE).workDay(UPDATED_WORK_DAY);

        restSalaryDistributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryDistribute.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalaryDistribute))
            )
            .andExpect(status().isOk());

        // Validate the SalaryDistribute in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalaryDistributeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSalaryDistribute, salaryDistribute),
            getPersistedSalaryDistribute(salaryDistribute)
        );
    }

    @Test
    @Transactional
    void fullUpdateSalaryDistributeWithPatch() throws Exception {
        // Initialize the database
        insertedSalaryDistribute = salaryDistributeRepository.saveAndFlush(salaryDistribute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salaryDistribute using partial update
        SalaryDistribute partialUpdatedSalaryDistribute = new SalaryDistribute();
        partialUpdatedSalaryDistribute.setId(salaryDistribute.getId());

        partialUpdatedSalaryDistribute
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .workDay(UPDATED_WORK_DAY)
            .typeOfSalary(UPDATED_TYPE_OF_SALARY);

        restSalaryDistributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryDistribute.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalaryDistribute))
            )
            .andExpect(status().isOk());

        // Validate the SalaryDistribute in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalaryDistributeUpdatableFieldsEquals(
            partialUpdatedSalaryDistribute,
            getPersistedSalaryDistribute(partialUpdatedSalaryDistribute)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSalaryDistribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salaryDistribute.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryDistributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaryDistribute.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salaryDistribute))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalaryDistribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salaryDistribute.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDistributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salaryDistribute))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalaryDistribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salaryDistribute.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDistributeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(salaryDistribute)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryDistribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalaryDistribute() throws Exception {
        // Initialize the database
        insertedSalaryDistribute = salaryDistributeRepository.saveAndFlush(salaryDistribute);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the salaryDistribute
        restSalaryDistributeMockMvc
            .perform(delete(ENTITY_API_URL_ID, salaryDistribute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return salaryDistributeRepository.count();
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

    protected SalaryDistribute getPersistedSalaryDistribute(SalaryDistribute salaryDistribute) {
        return salaryDistributeRepository.findById(salaryDistribute.getId()).orElseThrow();
    }

    protected void assertPersistedSalaryDistributeToMatchAllProperties(SalaryDistribute expectedSalaryDistribute) {
        assertSalaryDistributeAllPropertiesEquals(expectedSalaryDistribute, getPersistedSalaryDistribute(expectedSalaryDistribute));
    }

    protected void assertPersistedSalaryDistributeToMatchUpdatableProperties(SalaryDistribute expectedSalaryDistribute) {
        assertSalaryDistributeAllUpdatablePropertiesEquals(
            expectedSalaryDistribute,
            getPersistedSalaryDistribute(expectedSalaryDistribute)
        );
    }
}
