package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Resignation.
 */
@Entity
@Table(name = "resignation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resignation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @NotNull
    @Size(max = 255)
    @Column(name = "reason", length = 255, nullable = false)
    private String reason;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;

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

    public Resignation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSubmissionDate() {
        return this.submissionDate;
    }

    public Resignation submissionDate(LocalDate submissionDate) {
        this.setSubmissionDate(submissionDate);
        return this;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public Resignation effectiveDate(LocalDate effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getReason() {
        return this.reason;
    }

    public Resignation reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return this.status;
    }

    public Resignation status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public Resignation notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Resignation employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resignation)) {
            return false;
        }
        return getId() != null && getId().equals(((Resignation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resignation{" +
            "id=" + getId() +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
