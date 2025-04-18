package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @NotNull
    @Size(max = 20)
    @Column(name = "contract_code", length = 20, nullable = false, unique = true)
    private String contractCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contracts" }, allowSetters = true)
    private ContractType contractType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @JsonIgnoreProperties(
        value = { "department", "contract", "attendances", "payrolls", "resignations", "rewardPunishments" },
        allowSetters = true
    )
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    private Set<ContractTermination> contractTerminations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Contract startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Contract endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Contract status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContractCode() {
        return this.contractCode;
    }

    public Contract contractCode(String contractCode) {
        this.setContractCode(contractCode);
        return this;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public ContractType getContractType() {
        return this.contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public Contract contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setContract(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setContract(this));
        }
        this.employees = employees;
    }

    public Contract employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Contract addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setContract(this);
        return this;
    }

    public Contract removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setContract(null);
        return this;
    }

    public Set<ContractTermination> getContractTerminations() {
        return this.contractTerminations;
    }

    public void setContractTerminations(Set<ContractTermination> contractTerminations) {
        if (this.contractTerminations != null) {
            this.contractTerminations.forEach(i -> i.setContract(null));
        }
        if (contractTerminations != null) {
            contractTerminations.forEach(i -> i.setContract(this));
        }
        this.contractTerminations = contractTerminations;
    }

    public Contract contractTerminations(Set<ContractTermination> contractTerminations) {
        this.setContractTerminations(contractTerminations);
        return this;
    }

    public Contract addContractTermination(ContractTermination contractTermination) {
        this.contractTerminations.add(contractTermination);
        contractTermination.setContract(this);
        return this;
    }

    public Contract removeContractTermination(ContractTermination contractTermination) {
        this.contractTerminations.remove(contractTermination);
        contractTermination.setContract(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return getId() != null && getId().equals(((Contract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", contractCode='" + getContractCode() + "'" +
            "}";
    }
}
