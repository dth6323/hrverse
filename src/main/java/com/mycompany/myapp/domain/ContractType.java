package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ContractType.
 */
@Entity
@Table(name = "contract_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "type_name", length = 50, nullable = false)
    private String typeName;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contractType")
    @JsonIgnoreProperties(value = { "contractType", "employees", "contractTerminations" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContractType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public ContractType typeName(String typeName) {
        this.setTypeName(typeName);
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return this.description;
    }

    public ContractType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setContractType(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setContractType(this));
        }
        this.contracts = contracts;
    }

    public ContractType contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public ContractType addContract(Contract contract) {
        this.contracts.add(contract);
        contract.setContractType(this);
        return this;
    }

    public ContractType removeContract(Contract contract) {
        this.contracts.remove(contract);
        contract.setContractType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractType)) {
            return false;
        }
        return getId() != null && getId().equals(((ContractType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractType{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
