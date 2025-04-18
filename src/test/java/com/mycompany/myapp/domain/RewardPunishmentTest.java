package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.RewardPunishmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RewardPunishmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RewardPunishment.class);
        RewardPunishment rewardPunishment1 = getRewardPunishmentSample1();
        RewardPunishment rewardPunishment2 = new RewardPunishment();
        assertThat(rewardPunishment1).isNotEqualTo(rewardPunishment2);

        rewardPunishment2.setId(rewardPunishment1.getId());
        assertThat(rewardPunishment1).isEqualTo(rewardPunishment2);

        rewardPunishment2 = getRewardPunishmentSample2();
        assertThat(rewardPunishment1).isNotEqualTo(rewardPunishment2);
    }

    @Test
    void employeeTest() {
        RewardPunishment rewardPunishment = getRewardPunishmentRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        rewardPunishment.setEmployee(employeeBack);
        assertThat(rewardPunishment.getEmployee()).isEqualTo(employeeBack);

        rewardPunishment.employee(null);
        assertThat(rewardPunishment.getEmployee()).isNull();
    }
}
