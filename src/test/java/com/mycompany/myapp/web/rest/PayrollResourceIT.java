package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PayrollAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Payroll;
import com.mycompany.myapp.repository.PayrollRepository;
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
 * Integration tests for the {@link PayrollResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PayrollResourceIT {

    private static final Integer DEFAULT_SALARY = 1;
    private static final Integer UPDATED_SALARY = 2;

    private static final Integer DEFAULT_WORK_DAY = 1;
    private static final Integer UPDATED_WORK_DAY = 2;

    private static final String ENTITY_API_URL = "/api/payrolls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayrollMockMvc;

    private Payroll payroll;

    private Payroll insertedPayroll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payroll createEntity() {
        return new Payroll().salary(DEFAULT_SALARY).workDay(DEFAULT_WORK_DAY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payroll createUpdatedEntity() {
        return new Payroll().salary(UPDATED_SALARY).workDay(UPDATED_WORK_DAY);
    }

    @BeforeEach
    public void initTest() {
        payroll = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPayroll != null) {
            payrollRepository.delete(insertedPayroll);
            insertedPayroll = null;
        }
    }

    @Test
    @Transactional
    void createPayroll() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payroll
        var returnedPayroll = om.readValue(
            restPayrollMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payroll)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Payroll.class
        );

        // Validate the Payroll in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPayrollUpdatableFieldsEquals(returnedPayroll, getPersistedPayroll(returnedPayroll));

        insertedPayroll = returnedPayroll;
    }

    @Test
    @Transactional
    void createPayrollWithExistingId() throws Exception {
        // Create the Payroll with an existing ID
        payroll.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayrollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payroll)))
            .andExpect(status().isBadRequest());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSalaryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payroll.setSalary(null);

        // Create the Payroll, which fails.

        restPayrollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payroll)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkDayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payroll.setWorkDay(null);

        // Create the Payroll, which fails.

        restPayrollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payroll)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayrolls() throws Exception {
        // Initialize the database
        insertedPayroll = payrollRepository.saveAndFlush(payroll);

        // Get all the payrollList
        restPayrollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payroll.getId().intValue())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.[*].workDay").value(hasItem(DEFAULT_WORK_DAY)));
    }

    @Test
    @Transactional
    void getPayroll() throws Exception {
        // Initialize the database
        insertedPayroll = payrollRepository.saveAndFlush(payroll);

        // Get the payroll
        restPayrollMockMvc
            .perform(get(ENTITY_API_URL_ID, payroll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payroll.getId().intValue()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY))
            .andExpect(jsonPath("$.workDay").value(DEFAULT_WORK_DAY));
    }

    @Test
    @Transactional
    void getNonExistingPayroll() throws Exception {
        // Get the payroll
        restPayrollMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayroll() throws Exception {
        // Initialize the database
        insertedPayroll = payrollRepository.saveAndFlush(payroll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payroll
        Payroll updatedPayroll = payrollRepository.findById(payroll.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayroll are not directly saved in db
        em.detach(updatedPayroll);
        updatedPayroll.salary(UPDATED_SALARY).workDay(UPDATED_WORK_DAY);

        restPayrollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayroll.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPayroll))
            )
            .andExpect(status().isOk());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPayrollToMatchAllProperties(updatedPayroll);
    }

    @Test
    @Transactional
    void putNonExistingPayroll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payroll.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayrollMockMvc
            .perform(put(ENTITY_API_URL_ID, payroll.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payroll)))
            .andExpect(status().isBadRequest());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayroll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payroll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayrollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(payroll))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayroll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payroll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayrollMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payroll)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePayrollWithPatch() throws Exception {
        // Initialize the database
        insertedPayroll = payrollRepository.saveAndFlush(payroll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payroll using partial update
        Payroll partialUpdatedPayroll = new Payroll();
        partialUpdatedPayroll.setId(payroll.getId());

        partialUpdatedPayroll.workDay(UPDATED_WORK_DAY);

        restPayrollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayroll.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayroll))
            )
            .andExpect(status().isOk());

        // Validate the Payroll in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPayrollUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayroll, payroll), getPersistedPayroll(payroll));
    }

    @Test
    @Transactional
    void fullUpdatePayrollWithPatch() throws Exception {
        // Initialize the database
        insertedPayroll = payrollRepository.saveAndFlush(payroll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payroll using partial update
        Payroll partialUpdatedPayroll = new Payroll();
        partialUpdatedPayroll.setId(payroll.getId());

        partialUpdatedPayroll.salary(UPDATED_SALARY).workDay(UPDATED_WORK_DAY);

        restPayrollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayroll.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayroll))
            )
            .andExpect(status().isOk());

        // Validate the Payroll in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPayrollUpdatableFieldsEquals(partialUpdatedPayroll, getPersistedPayroll(partialUpdatedPayroll));
    }

    @Test
    @Transactional
    void patchNonExistingPayroll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payroll.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayrollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payroll.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(payroll))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayroll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payroll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayrollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(payroll))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayroll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payroll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayrollMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(payroll)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payroll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayroll() throws Exception {
        // Initialize the database
        insertedPayroll = payrollRepository.saveAndFlush(payroll);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payroll
        restPayrollMockMvc
            .perform(delete(ENTITY_API_URL_ID, payroll.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return payrollRepository.count();
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

    protected Payroll getPersistedPayroll(Payroll payroll) {
        return payrollRepository.findById(payroll.getId()).orElseThrow();
    }

    protected void assertPersistedPayrollToMatchAllProperties(Payroll expectedPayroll) {
        assertPayrollAllPropertiesEquals(expectedPayroll, getPersistedPayroll(expectedPayroll));
    }

    protected void assertPersistedPayrollToMatchUpdatableProperties(Payroll expectedPayroll) {
        assertPayrollAllUpdatablePropertiesEquals(expectedPayroll, getPersistedPayroll(expectedPayroll));
    }
}
