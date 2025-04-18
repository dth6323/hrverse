package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ContractTerminationTestSamples.*;
import static com.mycompany.myapp.domain.ContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractTerminationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractTermination.class);
        ContractTermination contractTermination1 = getContractTerminationSample1();
        ContractTermination contractTermination2 = new ContractTermination();
        assertThat(contractTermination1).isNotEqualTo(contractTermination2);

        contractTermination2.setId(contractTermination1.getId());
        assertThat(contractTermination1).isEqualTo(contractTermination2);

        contractTermination2 = getContractTerminationSample2();
        assertThat(contractTermination1).isNotEqualTo(contractTermination2);
    }

    @Test
    void contractTest() {
        ContractTermination contractTermination = getContractTerminationRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        contractTermination.setContract(contractBack);
        assertThat(contractTermination.getContract()).isEqualTo(contractBack);

        contractTermination.contract(null);
        assertThat(contractTermination.getContract()).isNull();
    }
}
