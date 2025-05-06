package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Wage.
 */
@Entity
@Table(name = "wage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Wage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "coefficients", nullable = false)
    private Float coefficients;

    @NotNull
    @Column(name = "base_salary", nullable = false)
    private Float baseSalary;

    @NotNull
    @Column(name = "allowance", nullable = false)
    private Float allowance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wage")
    @JsonIgnoreProperties(value = { "wage", "contractType", "employees", "contractTerminations" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Wage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCoefficients() {
        return this.coefficients;
    }

    public Wage coefficients(Float coefficients) {
        this.setCoefficients(coefficients);
        return this;
    }

    public void setCoefficients(Float coefficients) {
        this.coefficients = coefficients;
    }

    public Float getBaseSalary() {
        return this.baseSalary;
    }

    public Wage baseSalary(Float baseSalary) {
        this.setBaseSalary(baseSalary);
        return this;
    }

    public void setBaseSalary(Float baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Float getAllowance() {
        return this.allowance;
    }

    public Wage allowance(Float allowance) {
        this.setAllowance(allowance);
        return this;
    }

    public void setAllowance(Float allowance) {
        this.allowance = allowance;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setWage(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setWage(this));
        }
        this.contracts = contracts;
    }

    public Wage contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Wage addContract(Contract contract) {
        this.contracts.add(contract);
        contract.setWage(this);
        return this;
    }

    public Wage removeContract(Contract contract) {
        this.contracts.remove(contract);
        contract.setWage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wage)) {
            return false;
        }
        return getId() != null && getId().equals(((Wage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wage{" +
            "id=" + getId() +
            ", coefficients=" + getCoefficients() +
            ", baseSalary=" + getBaseSalary() +
            ", allowance=" + getAllowance() +
            "}";
    }
}
