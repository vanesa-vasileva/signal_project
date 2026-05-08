package com.alerts.decorators;

import com.alerts.Alert;

public class PriorityAlertDecorator extends AlertDecorator {
    private String priority; // HIGH, MEDIUM, LOW

    public PriorityAlertDecorator(Alert wrappedAlert, String priority) {
        super(wrappedAlert);
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    @Override
    public String getCondition() {
        return "[PRIORITY: " + priority + "] " + wrappedAlert.getCondition();
    }
}