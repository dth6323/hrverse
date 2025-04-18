package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.PayrollTestSamples.*;
import static com.mycompany.myapp.domain.SalaryDistributeTestSamples.*;
import static com.mycompany.myapp.domain.WageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayrollTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payroll.class);
        Payroll payroll1 = getPayrollSample1();
        Payroll payroll2 = new Payroll();
        assertThat(payroll1).isNotEqualTo(payroll2);

        payroll2.setId(payroll1.getId());
        assertThat(payroll1).isEqualTo(payroll2);

        payroll2 = getPayrollSample2();
        assertThat(payroll1).isNotEqualTo(payroll2);
    }

    @Test
    void employeeTest() {
        Payroll payroll = getPayrollRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        payroll.setEmployee(employeeBack);
        assertThat(payroll.getEmployee()).isEqualTo(employeeBack);

        payroll.employee(null);
        assertThat(payroll.getEmployee()).isNull();
    }

    @Test
    void wageTest() {
        Payroll payroll = getPayrollRandomSampleGenerator();
        Wage wageBack = getWageRandomSampleGenerator();

        payroll.setWage(wageBack);
        assertThat(payroll.getWage()).isEqualTo(wageBack);

        payroll.wage(null);
        assertThat(payroll.getWage()).isNull();
    }

    @Test
    void salaryDistributeTest() {
        Payroll payroll = getPayrollRandomSampleGenerator();
        SalaryDistribute salaryDistributeBack = getSalaryDistributeRandomSampleGenerator();

        payroll.setSalaryDistribute(salaryDistributeBack);
        assertThat(payroll.getSalaryDistribute()).isEqualTo(salaryDistributeBack);

        payroll.salaryDistribute(null);
        assertThat(payroll.getSalaryDistribute()).isNull();
    }
}
