package com.alerts.strategies;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertStrategyTest {

    @Test
    public void testBloodPressureStrategyTriggersOnHighValue() {
        AlertStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert("P001", System.currentTimeMillis(), 190.0);
        assertNotNull(alert, "Alert should be triggered for high blood pressure (190)");
    }

    @Test
    public void testBloodPressureStrategyTriggersOnLowValue() {
        AlertStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert("P001", System.currentTimeMillis(), 40.0);
        assertNotNull(alert, "Alert should be triggered for low blood pressure (40)");
    }

    @Test
    public void testBloodPressureStrategyNoAlertForNormalValue() {
        AlertStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert("P001", System.currentTimeMillis(), 120.0);
        assertNull(alert, "No alert should be triggered for normal blood pressure (120)");
    }

    @Test
    public void testHeartRateStrategyTriggersOnHighValue() {
        AlertStrategy strategy = new HeartRateStrategy();
        Alert alert = strategy.checkAlert("P002", System.currentTimeMillis(), 130.0);
        assertNotNull(alert, "Alert should be triggered for high heart rate (130 bpm)");
    }

    @Test
    public void testHeartRateStrategyTriggersOnLowValue() {
        AlertStrategy strategy = new HeartRateStrategy();
        Alert alert = strategy.checkAlert("P002", System.currentTimeMillis(), 35.0);
        assertNotNull(alert, "Alert should be triggered for low heart rate (35 bpm)");
    }

    @Test
    public void testHeartRateStrategyNoAlertForNormalValue() {
        AlertStrategy strategy = new HeartRateStrategy();
        Alert alert = strategy.checkAlert("P002", System.currentTimeMillis(), 75.0);
        assertNull(alert, "No alert should be triggered for normal heart rate (75 bpm)");
    }

    @Test
    public void testOxygenSaturationStrategyTriggersOnLowValue() {
        AlertStrategy strategy = new OxygenSaturationStrategy();
        Alert alert = strategy.checkAlert("P003", System.currentTimeMillis(), 85.0);
        assertNotNull(alert, "Alert should be triggered for low oxygen saturation (85%)");
    }

    @Test
    public void testOxygenSaturationStrategyNoAlertForNormalValue() {
        AlertStrategy strategy = new OxygenSaturationStrategy();
        Alert alert = strategy.checkAlert("P003", System.currentTimeMillis(), 95.0);
        assertNull(alert, "No alert should be triggered for normal oxygen saturation (95%)");
    }
}