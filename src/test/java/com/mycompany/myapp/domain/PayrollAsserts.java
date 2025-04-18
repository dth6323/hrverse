package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PayrollAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPayrollAllPropertiesEquals(Payroll expected, Payroll actual) {
        assertPayrollAutoGeneratedPropertiesEquals(expected, actual);
        assertPayrollAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPayrollAllUpdatablePropertiesEquals(Payroll expected, Payroll actual) {
        assertPayrollUpdatableFieldsEquals(expected, actual);
        assertPayrollUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPayrollAutoGeneratedPropertiesEquals(Payroll expected, Payroll actual) {
        assertThat(expected)
            .as("Verify Payroll auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPayrollUpdatableFieldsEquals(Payroll expected, Payroll actual) {
        assertThat(expected)
            .as("Verify Payroll relevant properties")
            .satisfies(e -> assertThat(e.getSalary()).as("check salary").isEqualTo(actual.getSalary()))
            .satisfies(e -> assertThat(e.getWorkDay()).as("check workDay").isEqualTo(actual.getWorkDay()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPayrollUpdatableRelationshipsEquals(Payroll expected, Payroll actual) {
        assertThat(expected)
            .as("Verify Payroll relationships")
            .satisfies(e -> assertThat(e.getEmployee()).as("check employee").isEqualTo(actual.getEmployee()))
            .satisfies(e -> assertThat(e.getWage()).as("check wage").isEqualTo(actual.getWage()))
            .satisfies(e -> assertThat(e.getSalaryDistribute()).as("check salaryDistribute").isEqualTo(actual.getSalaryDistribute()));
    }
}
