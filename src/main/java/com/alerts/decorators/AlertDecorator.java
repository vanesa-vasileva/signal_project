package com.alerts.decorators;

import com.alerts.Alert;

public abstract class AlertDecorator extends Alert {
    protected Alert wrappedAlert;

    public AlertDecorator(Alert wrappedAlert) {
        super(wrappedAlert.getPatientId(), wrappedAlert.getCondition(), wrappedAlert.getTimestamp());
        this.wrappedAlert = wrappedAlert;
    }

    public Alert getWrappedAlert() {
        return wrappedAlert;
    }
}