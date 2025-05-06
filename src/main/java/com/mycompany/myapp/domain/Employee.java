package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(max = 20)
    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @NotNull
    @Size(max = 255)
    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @NotNull
    @Column(name = "gender", nullable = false)
    private Integer gender;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wage", "contractType", "employees", "contractTerminations" }, allowSetters = true)
    private Contract contract;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Attendance> attendances = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee", "salaryDistribute" }, allowSetters = true)
    private Set<Payroll> payrolls = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Resignation> resignations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<RewardPunishment> rewardPunishments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Employee name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Employee phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public Employee address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getGender() {
        return this.gender;
    }

    public Employee gender(Integer gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Employee dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Employee contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public Set<Attendance> getAttendances() {
        return this.attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        if (this.attendances != null) {
            this.attendances.forEach(i -> i.setEmployee(null));
        }
        if (attendances != null) {
            attendances.forEach(i -> i.setEmployee(this));
        }
        this.attendances = attendances;
    }

    public Employee attendances(Set<Attendance> attendances) {
        this.setAttendances(attendances);
        return this;
    }

    public Employee addAttendance(Attendance attendance) {
        this.attendances.add(attendance);
        attendance.setEmployee(this);
        return this;
    }

    public Employee removeAttendance(Attendance attendance) {
        this.attendances.remove(attendance);
        attendance.setEmployee(null);
        return this;
    }

    public Set<Payroll> getPayrolls() {
        return this.payrolls;
    }

    public void setPayrolls(Set<Payroll> payrolls) {
        if (this.payrolls != null) {
            this.payrolls.forEach(i -> i.setEmployee(null));
        }
        if (payrolls != null) {
            payrolls.forEach(i -> i.setEmployee(this));
        }
        this.payrolls = payrolls;
    }

    public Employee payrolls(Set<Payroll> payrolls) {
        this.setPayrolls(payrolls);
        return this;
    }

    public Employee addPayroll(Payroll payroll) {
        this.payrolls.add(payroll);
        payroll.setEmployee(this);
        return this;
    }

    public Employee removePayroll(Payroll payroll) {
        this.payrolls.remove(payroll);
        payroll.setEmployee(null);
        return this;
    }

    public Set<Resignation> getResignations() {
        return this.resignations;
    }

    public void setResignations(Set<Resignation> resignations) {
        if (this.resignations != null) {
            this.resignations.forEach(i -> i.setEmployee(null));
        }
        if (resignations != null) {
            resignations.forEach(i -> i.setEmployee(this));
        }
        this.resignations = resignations;
    }

    public Employee resignations(Set<Resignation> resignations) {
        this.setResignations(resignations);
        return this;
    }

    public Employee addResignation(Resignation resignation) {
        this.resignations.add(resignation);
        resignation.setEmployee(this);
        return this;
    }

    public Employee removeResignation(Resignation resignation) {
        this.resignations.remove(resignation);
        resignation.setEmployee(null);
        return this;
    }

    public Set<RewardPunishment> getRewardPunishments() {
        return this.rewardPunishments;
    }

    public void setRewardPunishments(Set<RewardPunishment> rewardPunishments) {
        if (this.rewardPunishments != null) {
            this.rewardPunishments.forEach(i -> i.setEmployee(null));
        }
        if (rewardPunishments != null) {
            rewardPunishments.forEach(i -> i.setEmployee(this));
        }
        this.rewardPunishments = rewardPunishments;
    }

    public Employee rewardPunishments(Set<RewardPunishment> rewardPunishments) {
        this.setRewardPunishments(rewardPunishments);
        return this;
    }

    public Employee addRewardPunishment(RewardPunishment rewardPunishment) {
        this.rewardPunishments.add(rewardPunishment);
        rewardPunishment.setEmployee(this);
        return this;
    }

    public Employee removeRewardPunishment(RewardPunishment rewardPunishment) {
        this.rewardPunishments.remove(rewardPunishment);
        rewardPunishment.setEmployee(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            ", gender=" + getGender() +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            "}";
    }
}
