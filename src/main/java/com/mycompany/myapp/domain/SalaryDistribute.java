package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A SalaryDistribute.
 */
@Entity
@Table(name = "salary_distribute")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryDistribute implements Serializable {

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
    @Column(name = "work_day", nullable = false)
    private Integer workDay;

    @NotNull
    @Size(max = 30)
    @Column(name = "type_of_salary", length = 30, nullable = false)
    private String typeOfSalary;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salaryDistribute")
    @JsonIgnoreProperties(value = { "employee", "salaryDistribute" }, allowSetters = true)
    private Set<Payroll> payrolls = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SalaryDistribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public SalaryDistribute startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public SalaryDistribute endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getWorkDay() {
        return this.workDay;
    }

    public SalaryDistribute workDay(Integer workDay) {
        this.setWorkDay(workDay);
        return this;
    }

    public void setWorkDay(Integer workDay) {
        this.workDay = workDay;
    }

    public String getTypeOfSalary() {
        return this.typeOfSalary;
    }

    public SalaryDistribute typeOfSalary(String typeOfSalary) {
        this.setTypeOfSalary(typeOfSalary);
        return this;
    }

    public void setTypeOfSalary(String typeOfSalary) {
        this.typeOfSalary = typeOfSalary;
    }

    public Set<Payroll> getPayrolls() {
        return this.payrolls;
    }

    public void setPayrolls(Set<Payroll> payrolls) {
        if (this.payrolls != null) {
            this.payrolls.forEach(i -> i.setSalaryDistribute(null));
        }
        if (payrolls != null) {
            payrolls.forEach(i -> i.setSalaryDistribute(this));
        }
        this.payrolls = payrolls;
    }

    public SalaryDistribute payrolls(Set<Payroll> payrolls) {
        this.setPayrolls(payrolls);
        return this;
    }

    public SalaryDistribute addPayroll(Payroll payroll) {
        this.payrolls.add(payroll);
        payroll.setSalaryDistribute(this);
        return this;
    }

    public SalaryDistribute removePayroll(Payroll payroll) {
        this.payrolls.remove(payroll);
        payroll.setSalaryDistribute(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalaryDistribute)) {
            return false;
        }
        return getId() != null && getId().equals(((SalaryDistribute) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryDistribute{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", workDay=" + getWorkDay() +
            ", typeOfSalary='" + getTypeOfSalary() + "'" +
            "}";
    }
}
