package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        return new Employee().id(1L).name("name1").phone("phone1").email("email1").address("address1").gender(1);
    }

    public static Employee getEmployeeSample2() {
        return new Employee().id(2L).name("name2").phone("phone2").email("email2").address("address2").gender(2);
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        return new Employee()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .gender(intCount.incrementAndGet());
    }
}
