package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ContractTermination.
 */
@Entity
@Table(name = "contract_termination")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractTermination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "termination_date", nullable = false)
    private LocalDate terminationDate;

    @NotNull
    @Size(max = 255)
    @Column(name = "reason", length = 255, nullable = false)
    private String reason;

    @Column(name = "compensation")
    private Float compensation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wage", "contractType", "employees", "contractTerminations" }, allowSetters = true)
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContractTermination id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTerminationDate() {
        return this.terminationDate;
    }

    public ContractTermination terminationDate(LocalDate terminationDate) {
        this.setTerminationDate(terminationDate);
        return this;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getReason() {
        return this.reason;
    }

    public ContractTermination reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Float getCompensation() {
        return this.compensation;
    }

    public ContractTermination compensation(Float compensation) {
        this.setCompensation(compensation);
        return this;
    }

    public void setCompensation(Float compensation) {
        this.compensation = compensation;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public ContractTermination contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractTermination)) {
            return false;
        }
        return getId() != null && getId().equals(((ContractTermination) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractTermination{" +
            "id=" + getId() +
            ", terminationDate='" + getTerminationDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", compensation=" + getCompensation() +
            "}";
    }
}
