package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PayrollTestSamples.*;
import static com.mycompany.myapp.domain.WageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wage.class);
        Wage wage1 = getWageSample1();
        Wage wage2 = new Wage();
        assertThat(wage1).isNotEqualTo(wage2);

        wage2.setId(wage1.getId());
        assertThat(wage1).isEqualTo(wage2);

        wage2 = getWageSample2();
        assertThat(wage1).isNotEqualTo(wage2);
    }

    @Test
    void payrollTest() {
        Wage wage = getWageRandomSampleGenerator();
        Payroll payrollBack = getPayrollRandomSampleGenerator();

        wage.addPayroll(payrollBack);
        assertThat(wage.getPayrolls()).containsOnly(payrollBack);
        assertThat(payrollBack.getWage()).isEqualTo(wage);

        wage.removePayroll(payrollBack);
        assertThat(wage.getPayrolls()).doesNotContain(payrollBack);
        assertThat(payrollBack.getWage()).isNull();

        wage.payrolls(new HashSet<>(Set.of(payrollBack)));
        assertThat(wage.getPayrolls()).containsOnly(payrollBack);
        assertThat(payrollBack.getWage()).isEqualTo(wage);

        wage.setPayrolls(new HashSet<>());
        assertThat(wage.getPayrolls()).doesNotContain(payrollBack);
        assertThat(payrollBack.getWage()).isNull();
    }
}
