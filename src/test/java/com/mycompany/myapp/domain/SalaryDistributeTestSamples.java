package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SalaryDistributeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SalaryDistribute getSalaryDistributeSample1() {
        return new SalaryDistribute().id(1L).workDay(1).typeOfSalary("typeOfSalary1");
    }

    public static SalaryDistribute getSalaryDistributeSample2() {
        return new SalaryDistribute().id(2L).workDay(2).typeOfSalary("typeOfSalary2");
    }

    public static SalaryDistribute getSalaryDistributeRandomSampleGenerator() {
        return new SalaryDistribute()
            .id(longCount.incrementAndGet())
            .workDay(intCount.incrementAndGet())
            .typeOfSalary(UUID.randomUUID().toString());
    }
}
