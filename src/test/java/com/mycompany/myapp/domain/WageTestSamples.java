package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class WageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Wage getWageSample1() {
        return new Wage().id(1L);
    }

    public static Wage getWageSample2() {
        return new Wage().id(2L);
    }

    public static Wage getWageRandomSampleGenerator() {
        return new Wage().id(longCount.incrementAndGet());
    }
}
