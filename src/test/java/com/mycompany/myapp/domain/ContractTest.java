package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ContractTerminationTestSamples.*;
import static com.mycompany.myapp.domain.ContractTestSamples.*;
import static com.mycompany.myapp.domain.ContractTypeTestSamples.*;
import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.WageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void wageTest() {
        Contract contract = getContractRandomSampleGenerator();
        Wage wageBack = getWageRandomSampleGenerator();

        contract.setWage(wageBack);
        assertThat(contract.getWage()).isEqualTo(wageBack);

        contract.wage(null);
        assertThat(contract.getWage()).isNull();
    }

    @Test
    void contractTypeTest() {
        Contract contract = getContractRandomSampleGenerator();
        ContractType contractTypeBack = getContractTypeRandomSampleGenerator();

        contract.setContractType(contractTypeBack);
        assertThat(contract.getContractType()).isEqualTo(contractTypeBack);

        contract.contractType(null);
        assertThat(contract.getContractType()).isNull();
    }

    @Test
    void employeeTest() {
        Contract contract = getContractRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        contract.addEmployee(employeeBack);
        assertThat(contract.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getContract()).isEqualTo(contract);

        contract.removeEmployee(employeeBack);
        assertThat(contract.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getContract()).isNull();

        contract.employees(new HashSet<>(Set.of(employeeBack)));
        assertThat(contract.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getContract()).isEqualTo(contract);

        contract.setEmployees(new HashSet<>());
        assertThat(contract.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getContract()).isNull();
    }

    @Test
    void contractTerminationTest() {
        Contract contract = getContractRandomSampleGenerator();
        ContractTermination contractTerminationBack = getContractTerminationRandomSampleGenerator();

        contract.addContractTermination(contractTerminationBack);
        assertThat(contract.getContractTerminations()).containsOnly(contractTerminationBack);
        assertThat(contractTerminationBack.getContract()).isEqualTo(contract);

        contract.removeContractTermination(contractTerminationBack);
        assertThat(contract.getContractTerminations()).doesNotContain(contractTerminationBack);
        assertThat(contractTerminationBack.getContract()).isNull();

        contract.contractTerminations(new HashSet<>(Set.of(contractTerminationBack)));
        assertThat(contract.getContractTerminations()).containsOnly(contractTerminationBack);
        assertThat(contractTerminationBack.getContract()).isEqualTo(contract);

        contract.setContractTerminations(new HashSet<>());
        assertThat(contract.getContractTerminations()).doesNotContain(contractTerminationBack);
        assertThat(contractTerminationBack.getContract()).isNull();
    }
}
