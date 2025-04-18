package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContractTerminationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContractTermination getContractTerminationSample1() {
        return new ContractTermination().id(1L).reason("reason1");
    }

    public static ContractTermination getContractTerminationSample2() {
        return new ContractTermination().id(2L).reason("reason2");
    }

    public static ContractTermination getContractTerminationRandomSampleGenerator() {
        return new ContractTermination().id(longCount.incrementAndGet()).reason(UUID.randomUUID().toString());
    }
}
