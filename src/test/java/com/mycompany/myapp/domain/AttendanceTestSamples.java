package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AttendanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Attendance getAttendanceSample1() {
        return new Attendance().id(1L);
    }

    public static Attendance getAttendanceSample2() {
        return new Attendance().id(2L);
    }

    public static Attendance getAttendanceRandomSampleGenerator() {
        return new Attendance().id(longCount.incrementAndGet());
    }
}
