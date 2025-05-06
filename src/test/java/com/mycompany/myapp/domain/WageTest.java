package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ContractTestSamples.*;
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
    void contractTest() {
        Wage wage = getWageRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        wage.addContract(contractBack);
        assertThat(wage.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getWage()).isEqualTo(wage);

        wage.removeContract(contractBack);
        assertThat(wage.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getWage()).isNull();

        wage.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(wage.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getWage()).isEqualTo(wage);

        wage.setContracts(new HashSet<>());
        assertThat(wage.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getWage()).isNull();
    }
}
