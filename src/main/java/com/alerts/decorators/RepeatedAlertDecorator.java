package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Decorator that adds a repeat count to an alert.
 * Indicates how many times the alert has been repeated.
 */
public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatCount;

    /**
     * Creates a repeat counter decorator for an alert.
     *
     * @param wrappedAlert the alert to decorate
     * @param repeatCount how many times this alert has been repeated
     */
    public RepeatedAlertDecorator(Alert wrappedAlert, int repeatCount) {
        super(wrappedAlert);
        this.repeatCount = repeatCount;
    }

    /**
     * Returns the number of times this alert has been repeated.
     *
     * @return repeat count
     */
    public int getRepeatCount() {
        return repeatCount;
    }

    /**
     * Returns the condition string with repeat prefix.
     *
     * @return condition in format "[REPEAT Xx] original condition"
     */
    @Override
    public String getCondition() {
        return "[REPEAT " + repeatCount + "x] " + wrappedAlert.getCondition();
    }
}