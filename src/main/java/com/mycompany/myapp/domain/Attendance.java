package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Attendance.
 */
@Entity
@Table(name = "attendance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_ofwork", nullable = false)
    private LocalDate dateOfwork;

    @NotNull
    @Column(name = "check_in_time", nullable = false)
    private Instant checkInTime;

    @NotNull
    @Column(name = "check_out_time", nullable = false)
    private Instant checkOutTime;

    @NotNull
    @Column(name = "work_hour", nullable = false)
    private Float workHour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "department", "contract", "attendances", "payrolls", "resignations", "rewardPunishments" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attendance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfwork() {
        return this.dateOfwork;
    }

    public Attendance dateOfwork(LocalDate dateOfwork) {
        this.setDateOfwork(dateOfwork);
        return this;
    }

    public void setDateOfwork(LocalDate dateOfwork) {
        this.dateOfwork = dateOfwork;
    }

    public Instant getCheckInTime() {
        return this.checkInTime;
    }

    public Attendance checkInTime(Instant checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(Instant checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Instant getCheckOutTime() {
        return this.checkOutTime;
    }

    public Attendance checkOutTime(Instant checkOutTime) {
        this.setCheckOutTime(checkOutTime);
        return this;
    }

    public void setCheckOutTime(Instant checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Float getWorkHour() {
        return this.workHour;
    }

    public Attendance workHour(Float workHour) {
        this.setWorkHour(workHour);
        return this;
    }

    public void setWorkHour(Float workHour) {
        this.workHour = workHour;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Attendance employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attendance)) {
            return false;
        }
        return getId() != null && getId().equals(((Attendance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attendance{" +
            "id=" + getId() +
            ", dateOfwork='" + getDateOfwork() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            ", workHour=" + getWorkHour() +
            "}";
    }
}
