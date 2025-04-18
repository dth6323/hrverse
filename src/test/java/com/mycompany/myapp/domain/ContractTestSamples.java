package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContractTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contract getContractSample1() {
        return new Contract().id(1L).status("status1").contractCode("contractCode1");
    }

    public static Contract getContractSample2() {
        return new Contract().id(2L).status("status2").contractCode("contractCode2");
    }

    public static Contract getContractRandomSampleGenerator() {
        return new Contract()
            .id(longCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .contractCode(UUID.randomUUID().toString());
    }
}
