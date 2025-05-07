package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResignationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Resignation getResignationSample1() {
        return new Resignation().id(1L).reason("reason1").notes("notes1");
    }

    public static Resignation getResignationSample2() {
        return new Resignation().id(2L).reason("reason2").notes("notes2");
    }

    public static Resignation getResignationRandomSampleGenerator() {
        return new Resignation().id(longCount.incrementAndGet()).reason(UUID.randomUUID().toString()).notes(UUID.randomUUID().toString());
    }
}
