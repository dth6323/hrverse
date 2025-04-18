package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PayrollTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Payroll getPayrollSample1() {
        return new Payroll().id(1L).salary(1).workDay(1);
    }

    public static Payroll getPayrollSample2() {
        return new Payroll().id(2L).salary(2).workDay(2);
    }

    public static Payroll getPayrollRandomSampleGenerator() {
        return new Payroll().id(longCount.incrementAndGet()).salary(intCount.incrementAndGet()).workDay(intCount.incrementAndGet());
    }
}
