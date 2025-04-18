package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.ResignationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResignationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resignation.class);
        Resignation resignation1 = getResignationSample1();
        Resignation resignation2 = new Resignation();
        assertThat(resignation1).isNotEqualTo(resignation2);

        resignation2.setId(resignation1.getId());
        assertThat(resignation1).isEqualTo(resignation2);

        resignation2 = getResignationSample2();
        assertThat(resignation1).isNotEqualTo(resignation2);
    }

    @Test
    void employeeTest() {
        Resignation resignation = getResignationRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        resignation.setEmployee(employeeBack);
        assertThat(resignation.getEmployee()).isEqualTo(employeeBack);

        resignation.employee(null);
        assertThat(resignation.getEmployee()).isNull();
    }
}
