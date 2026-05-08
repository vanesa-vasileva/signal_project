package com.alerts.decorators;

import com.alerts.Alert;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatCount;

    public RepeatedAlertDecorator(Alert wrappedAlert, int repeatCount) {
        super(wrappedAlert);
        this.repeatCount = repeatCount;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    @Override
    public String getCondition() {
        return "[REPEAT " + repeatCount + "x] " + wrappedAlert.getCondition();
    }
}