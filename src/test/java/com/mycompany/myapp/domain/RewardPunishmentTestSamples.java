package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RewardPunishmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RewardPunishment getRewardPunishmentSample1() {
        return new RewardPunishment().id(1L).type("type1").reason("reason1").notes("notes1");
    }

    public static RewardPunishment getRewardPunishmentSample2() {
        return new RewardPunishment().id(2L).type("type2").reason("reason2").notes("notes2");
    }

    public static RewardPunishment getRewardPunishmentRandomSampleGenerator() {
        return new RewardPunishment()
            .id(longCount.incrementAndGet())
            .type(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
