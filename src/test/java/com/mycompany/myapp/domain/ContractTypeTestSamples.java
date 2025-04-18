package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContractTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContractType getContractTypeSample1() {
        return new ContractType().id(1L).typeName("typeName1").description("description1");
    }

    public static ContractType getContractTypeSample2() {
        return new ContractType().id(2L).typeName("typeName2").description("description2");
    }

    public static ContractType getContractTypeRandomSampleGenerator() {
        return new ContractType()
            .id(longCount.incrementAndGet())
            .typeName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
