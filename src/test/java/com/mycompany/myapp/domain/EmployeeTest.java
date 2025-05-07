package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AttendanceTestSamples.*;
import static com.mycompany.myapp.domain.ContractTestSamples.*;
import static com.mycompany.myapp.domain.DepartmentTestSamples.*;
import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.PayrollTestSamples.*;
import static com.mycompany.myapp.domain.ResignationTestSamples.*;
import static com.mycompany.myapp.domain.RewardPunishmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void departmentTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        employee.setDepartment(departmentBack);
        assertThat(employee.getDepartment()).isEqualTo(departmentBack);

        employee.department(null);
        assertThat(employee.getDepartment()).isNull();
    }

    @Test
    void contractTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        employee.addContract(contractBack);
        assertThat(employee.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getEmployee()).isEqualTo(employee);

        employee.removeContract(contractBack);
        assertThat(employee.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getEmployee()).isNull();

        employee.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(employee.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getEmployee()).isEqualTo(employee);

        employee.setContracts(new HashSet<>());
        assertThat(employee.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getEmployee()).isNull();
    }

    @Test
    void attendanceTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Attendance attendanceBack = getAttendanceRandomSampleGenerator();

        employee.addAttendance(attendanceBack);
        assertThat(employee.getAttendances()).containsOnly(attendanceBack);
        assertThat(attendanceBack.getEmployee()).isEqualTo(employee);

        employee.removeAttendance(attendanceBack);
        assertThat(employee.getAttendances()).doesNotContain(attendanceBack);
        assertThat(attendanceBack.getEmployee()).isNull();

        employee.attendances(new HashSet<>(Set.of(attendanceBack)));
        assertThat(employee.getAttendances()).containsOnly(attendanceBack);
        assertThat(attendanceBack.getEmployee()).isEqualTo(employee);

        employee.setAttendances(new HashSet<>());
        assertThat(employee.getAttendances()).doesNotContain(attendanceBack);
        assertThat(attendanceBack.getEmployee()).isNull();
    }

    @Test
    void payrollTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Payroll payrollBack = getPayrollRandomSampleGenerator();

        employee.addPayroll(payrollBack);
        assertThat(employee.getPayrolls()).containsOnly(payrollBack);
        assertThat(payrollBack.getEmployee()).isEqualTo(employee);

        employee.removePayroll(payrollBack);
        assertThat(employee.getPayrolls()).doesNotContain(payrollBack);
        assertThat(payrollBack.getEmployee()).isNull();

        employee.payrolls(new HashSet<>(Set.of(payrollBack)));
        assertThat(employee.getPayrolls()).containsOnly(payrollBack);
        assertThat(payrollBack.getEmployee()).isEqualTo(employee);

        employee.setPayrolls(new HashSet<>());
        assertThat(employee.getPayrolls()).doesNotContain(payrollBack);
        assertThat(payrollBack.getEmployee()).isNull();
    }

    @Test
    void resignationTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Resignation resignationBack = getResignationRandomSampleGenerator();

        employee.addResignation(resignationBack);
        assertThat(employee.getResignations()).containsOnly(resignationBack);
        assertThat(resignationBack.getEmployee()).isEqualTo(employee);

        employee.removeResignation(resignationBack);
        assertThat(employee.getResignations()).doesNotContain(resignationBack);
        assertThat(resignationBack.getEmployee()).isNull();

        employee.resignations(new HashSet<>(Set.of(resignationBack)));
        assertThat(employee.getResignations()).containsOnly(resignationBack);
        assertThat(resignationBack.getEmployee()).isEqualTo(employee);

        employee.setResignations(new HashSet<>());
        assertThat(employee.getResignations()).doesNotContain(resignationBack);
        assertThat(resignationBack.getEmployee()).isNull();
    }

    @Test
    void rewardPunishmentTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        RewardPunishment rewardPunishmentBack = getRewardPunishmentRandomSampleGenerator();

        employee.addRewardPunishment(rewardPunishmentBack);
        assertThat(employee.getRewardPunishments()).containsOnly(rewardPunishmentBack);
        assertThat(rewardPunishmentBack.getEmployee()).isEqualTo(employee);

        employee.removeRewardPunishment(rewardPunishmentBack);
        assertThat(employee.getRewardPunishments()).doesNotContain(rewardPunishmentBack);
        assertThat(rewardPunishmentBack.getEmployee()).isNull();

        employee.rewardPunishments(new HashSet<>(Set.of(rewardPunishmentBack)));
        assertThat(employee.getRewardPunishments()).containsOnly(rewardPunishmentBack);
        assertThat(rewardPunishmentBack.getEmployee()).isEqualTo(employee);

        employee.setRewardPunishments(new HashSet<>());
        assertThat(employee.getRewardPunishments()).doesNotContain(rewardPunishmentBack);
        assertThat(rewardPunishmentBack.getEmployee()).isNull();
    }
}
