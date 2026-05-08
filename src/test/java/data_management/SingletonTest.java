package com.data_management;

import com.cardio_generator.HealthDataSimulator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @Test
    public void testHealthDataSimulatorSingleton() {
        HealthDataSimulator hs1 = HealthDataSimulator.getInstance();
        HealthDataSimulator hs2 = HealthDataSimulator.getInstance();
        assertSame(hs1, hs2, "HealthDataSimulator instances are not the same");
    }
}