package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Payroll.
 */
@Entity
@Table(name = "payroll")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payroll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "salary", nullable = false)
    private Integer salary;

    @NotNull
    @Column(name = "work_day", nullable = false)
    private Integer workDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "department", "contract", "attendances", "payrolls", "resignations", "rewardPunishments" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "payrolls" }, allowSetters = true)
    private Wage wage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "payrolls" }, allowSetters = true)
    private SalaryDistribute salaryDistribute;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payroll id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSalary() {
        return this.salary;
    }

    public Payroll salary(Integer salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getWorkDay() {
        return this.workDay;
    }

    public Payroll workDay(Integer workDay) {
        this.setWorkDay(workDay);
        return this;
    }

    public void setWorkDay(Integer workDay) {
        this.workDay = workDay;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Payroll employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Wage getWage() {
        return this.wage;
    }

    public void setWage(Wage wage) {
        this.wage = wage;
    }

    public Payroll wage(Wage wage) {
        this.setWage(wage);
        return this;
    }

    public SalaryDistribute getSalaryDistribute() {
        return this.salaryDistribute;
    }

    public void setSalaryDistribute(SalaryDistribute salaryDistribute) {
        this.salaryDistribute = salaryDistribute;
    }

    public Payroll salaryDistribute(SalaryDistribute salaryDistribute) {
        this.setSalaryDistribute(salaryDistribute);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payroll)) {
            return false;
        }
        return getId() != null && getId().equals(((Payroll) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payroll{" +
            "id=" + getId() +
            ", salary=" + getSalary() +
            ", workDay=" + getWorkDay() +
            "}";
    }
}
