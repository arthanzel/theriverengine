package com.arthanzel.theriverengine.common.ui.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that a field should be editable in UIs as a double value using
 * a spinner control.
 *
 * @author Martin
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleSpinnerBinding {
    /**
     * Defines the minimum value imposed by the spinner.
     */
    double min();

    /**
     * Defines the maximum value imposed by the spinner.
     */
    double max();

    /**
     * Defines the step size imposed by the spinner.
     */
    double step() default 0.25;
}
