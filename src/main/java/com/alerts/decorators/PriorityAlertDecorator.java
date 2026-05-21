package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Decorator that adds a priority level to an alert.
 * Priority can be HIGH, MEDIUM, or LOW.
 * The priority is shown in the alert's condition string.
 */
public class PriorityAlertDecorator extends AlertDecorator {
    private String priority;

    /**
     * Creates a priority decorator for an alert.
     *
     * @param wrappedAlert the alert to decorate
     * @param priority the priority level (HIGH, MEDIUM, LOW)
     */
    public PriorityAlertDecorator(Alert wrappedAlert, String priority) {
        super(wrappedAlert);
        this.priority = priority;
    }

    /**
     * Returns the priority level of this alert.
     *
     * @return the priority string
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Returns the condition string with priority prefix.
     *
     * @return condition in format "[PRIORITY: X] original condition"
     */
    @Override
    public String getCondition() {
        return "[PRIORITY: " + priority + "] " + wrappedAlert.getCondition();
    }
}