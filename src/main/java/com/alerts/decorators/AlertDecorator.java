package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Base class for decorating an Alert with additional behavior.
 * Wraps an existing Alert and delegates calls to it.
 */
public abstract class AlertDecorator extends Alert {
    protected Alert wrappedAlert;

    /**
     * Creates a decorator that wraps another Alert.
     *
     * @param wrappedAlert the Alert to be decorated
     */
    public AlertDecorator(Alert wrappedAlert) {
        super(wrappedAlert.getPatientId(), wrappedAlert.getCondition(), wrappedAlert.getTimestamp());
        this.wrappedAlert = wrappedAlert;
    }

    /**
     * Returns the wrapped Alert instance.
     *
     * @return the inner Alert
     */
    public Alert getWrappedAlert() {
        return wrappedAlert;
    }
}