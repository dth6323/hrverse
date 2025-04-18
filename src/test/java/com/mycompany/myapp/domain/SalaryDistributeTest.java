package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PayrollTestSamples.*;
import static com.mycompany.myapp.domain.SalaryDistributeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SalaryDistributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryDistribute.class);
        SalaryDistribute salaryDistribute1 = getSalaryDistributeSample1();
        SalaryDistribute salaryDistribute2 = new SalaryDistribute();
        assertThat(salaryDistribute1).isNotEqualTo(salaryDistribute2);

        salaryDistribute2.setId(salaryDistribute1.getId());
        assertThat(salaryDistribute1).isEqualTo(salaryDistribute2);

        salaryDistribute2 = getSalaryDistributeSample2();
        assertThat(salaryDistribute1).isNotEqualTo(salaryDistribute2);
    }

    @Test
    void payrollTest() {
        SalaryDistribute salaryDistribute = getSalaryDistributeRandomSampleGenerator();
        Payroll payrollBack = getPayrollRandomSampleGenerator();

        salaryDistribute.addPayroll(payrollBack);
        assertThat(salaryDistribute.getPayrolls()).containsOnly(payrollBack);
        assertThat(payrollBack.getSalaryDistribute()).isEqualTo(salaryDistribute);

        salaryDistribute.removePayroll(payrollBack);
        assertThat(salaryDistribute.getPayrolls()).doesNotContain(payrollBack);
        assertThat(payrollBack.getSalaryDistribute()).isNull();

        salaryDistribute.payrolls(new HashSet<>(Set.of(payrollBack)));
        assertThat(salaryDistribute.getPayrolls()).containsOnly(payrollBack);
        assertThat(payrollBack.getSalaryDistribute()).isEqualTo(salaryDistribute);

        salaryDistribute.setPayrolls(new HashSet<>());
        assertThat(salaryDistribute.getPayrolls()).doesNotContain(payrollBack);
        assertThat(payrollBack.getSalaryDistribute()).isNull();
    }
}
