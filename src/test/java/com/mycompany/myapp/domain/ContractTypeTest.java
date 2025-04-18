package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ContractTestSamples.*;
import static com.mycompany.myapp.domain.ContractTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContractTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractType.class);
        ContractType contractType1 = getContractTypeSample1();
        ContractType contractType2 = new ContractType();
        assertThat(contractType1).isNotEqualTo(contractType2);

        contractType2.setId(contractType1.getId());
        assertThat(contractType1).isEqualTo(contractType2);

        contractType2 = getContractTypeSample2();
        assertThat(contractType1).isNotEqualTo(contractType2);
    }

    @Test
    void contractTest() {
        ContractType contractType = getContractTypeRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        contractType.addContract(contractBack);
        assertThat(contractType.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getContractType()).isEqualTo(contractType);

        contractType.removeContract(contractBack);
        assertThat(contractType.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getContractType()).isNull();

        contractType.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(contractType.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getContractType()).isEqualTo(contractType);

        contractType.setContracts(new HashSet<>());
        assertThat(contractType.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getContractType()).isNull();
    }
}
