package com.alerts.decorators;

import com.alerts.Alert;
import com.alerts.BloodPressureAlert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertDecoratorTest {

    @Test
    public void testPriorityDecorator() {
        Alert baseAlert = new BloodPressureAlert("P001", "High BP", System.currentTimeMillis());
        PriorityAlertDecorator decorated = new PriorityAlertDecorator(baseAlert, "HIGH");

        assertTrue(decorated.getCondition().contains("PRIORITY: HIGH"));
        assertEquals("HIGH", decorated.getPriority());
    }

    @Test
    public void testRepeatedDecorator() {
        Alert baseAlert = new BloodPressureAlert("P002", "Low BP", System.currentTimeMillis());
        RepeatedAlertDecorator decorated = new RepeatedAlertDecorator(baseAlert, 3);

        assertTrue(decorated.getCondition().contains("REPEAT 3x"));
        assertEquals(3, decorated.getRepeatCount());
    }
}
