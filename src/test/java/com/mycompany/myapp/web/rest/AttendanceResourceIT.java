package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AttendanceAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Attendance;
import com.mycompany.myapp.repository.AttendanceRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AttendanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttendanceResourceIT {

    private static final LocalDate DEFAULT_DATE_OFWORK = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OFWORK = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CHECK_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_WORK_HOUR = 1F;
    private static final Float UPDATED_WORK_HOUR = 2F;

    private static final String ENTITY_API_URL = "/api/attendances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttendanceMockMvc;

    private Attendance attendance;

    private Attendance insertedAttendance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attendance createEntity() {
        return new Attendance()
            .dateOfwork(DEFAULT_DATE_OFWORK)
            .checkInTime(DEFAULT_CHECK_IN_TIME)
            .checkOutTime(DEFAULT_CHECK_OUT_TIME)
            .workHour(DEFAULT_WORK_HOUR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attendance createUpdatedEntity() {
        return new Attendance()
            .dateOfwork(UPDATED_DATE_OFWORK)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .workHour(UPDATED_WORK_HOUR);
    }

    @BeforeEach
    public void initTest() {
        attendance = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAttendance != null) {
            attendanceRepository.delete(insertedAttendance);
            insertedAttendance = null;
        }
    }

    @Test
    @Transactional
    void createAttendance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Attendance
        var returnedAttendance = om.readValue(
            restAttendanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Attendance.class
        );

        // Validate the Attendance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAttendanceUpdatableFieldsEquals(returnedAttendance, getPersistedAttendance(returnedAttendance));

        insertedAttendance = returnedAttendance;
    }

    @Test
    @Transactional
    void createAttendanceWithExistingId() throws Exception {
        // Create the Attendance with an existing ID
        attendance.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance)))
            .andExpect(status().isBadRequest());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateOfworkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attendance.setDateOfwork(null);

        // Create the Attendance, which fails.

        restAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCheckInTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attendance.setCheckInTime(null);

        // Create the Attendance, which fails.

        restAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCheckOutTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attendance.setCheckOutTime(null);

        // Create the Attendance, which fails.

        restAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkHourIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attendance.setWorkHour(null);

        // Create the Attendance, which fails.

        restAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttendances() throws Exception {
        // Initialize the database
        insertedAttendance = attendanceRepository.saveAndFlush(attendance);

        // Get all the attendanceList
        restAttendanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attendance.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfwork").value(hasItem(DEFAULT_DATE_OFWORK.toString())))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(DEFAULT_CHECK_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkOutTime").value(hasItem(DEFAULT_CHECK_OUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].workHour").value(hasItem(DEFAULT_WORK_HOUR.doubleValue())));
    }

    @Test
    @Transactional
    void getAttendance() throws Exception {
        // Initialize the database
        insertedAttendance = attendanceRepository.saveAndFlush(attendance);

        // Get the attendance
        restAttendanceMockMvc
            .perform(get(ENTITY_API_URL_ID, attendance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attendance.getId().intValue()))
            .andExpect(jsonPath("$.dateOfwork").value(DEFAULT_DATE_OFWORK.toString()))
            .andExpect(jsonPath("$.checkInTime").value(DEFAULT_CHECK_IN_TIME.toString()))
            .andExpect(jsonPath("$.checkOutTime").value(DEFAULT_CHECK_OUT_TIME.toString()))
            .andExpect(jsonPath("$.workHour").value(DEFAULT_WORK_HOUR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAttendance() throws Exception {
        // Get the attendance
        restAttendanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttendance() throws Exception {
        // Initialize the database
        insertedAttendance = attendanceRepository.saveAndFlush(attendance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attendance
        Attendance updatedAttendance = attendanceRepository.findById(attendance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAttendance are not directly saved in db
        em.detach(updatedAttendance);
        updatedAttendance
            .dateOfwork(UPDATED_DATE_OFWORK)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .workHour(UPDATED_WORK_HOUR);

        restAttendanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttendance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAttendance))
            )
            .andExpect(status().isOk());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttendanceToMatchAllProperties(updatedAttendance);
    }

    @Test
    @Transactional
    void putNonExistingAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attendance.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attendance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttendanceWithPatch() throws Exception {
        // Initialize the database
        insertedAttendance = attendanceRepository.saveAndFlush(attendance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attendance using partial update
        Attendance partialUpdatedAttendance = new Attendance();
        partialUpdatedAttendance.setId(attendance.getId());

        partialUpdatedAttendance.checkInTime(UPDATED_CHECK_IN_TIME).checkOutTime(UPDATED_CHECK_OUT_TIME);

        restAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttendance))
            )
            .andExpect(status().isOk());

        // Validate the Attendance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttendanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttendance, attendance),
            getPersistedAttendance(attendance)
        );
    }

    @Test
    @Transactional
    void fullUpdateAttendanceWithPatch() throws Exception {
        // Initialize the database
        insertedAttendance = attendanceRepository.saveAndFlush(attendance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attendance using partial update
        Attendance partialUpdatedAttendance = new Attendance();
        partialUpdatedAttendance.setId(attendance.getId());

        partialUpdatedAttendance
            .dateOfwork(UPDATED_DATE_OFWORK)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .workHour(UPDATED_WORK_HOUR);

        restAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttendance))
            )
            .andExpect(status().isOk());

        // Validate the Attendance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttendanceUpdatableFieldsEquals(partialUpdatedAttendance, getPersistedAttendance(partialUpdatedAttendance));
    }

    @Test
    @Transactional
    void patchNonExistingAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attendance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(attendance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttendance() throws Exception {
        // Initialize the database
        insertedAttendance = attendanceRepository.saveAndFlush(attendance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the attendance
        restAttendanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, attendance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return attendanceRepository.count();
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

    protected Attendance getPersistedAttendance(Attendance attendance) {
        return attendanceRepository.findById(attendance.getId()).orElseThrow();
    }

    protected void assertPersistedAttendanceToMatchAllProperties(Attendance expectedAttendance) {
        assertAttendanceAllPropertiesEquals(expectedAttendance, getPersistedAttendance(expectedAttendance));
    }

    protected void assertPersistedAttendanceToMatchUpdatableProperties(Attendance expectedAttendance) {
        assertAttendanceAllUpdatablePropertiesEquals(expectedAttendance, getPersistedAttendance(expectedAttendance));
    }
}
