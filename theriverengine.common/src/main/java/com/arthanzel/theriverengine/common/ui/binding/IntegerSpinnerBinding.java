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
public @interface IntegerSpinnerBinding {
    /**
     * Defines the minimum value imposed by the spinner.
     */
    int min();

    /**
     * Defines the maximum value imposed by the spinner.
     */
    int max();

    /**
     * Defines the step size imposed by the spinner.
     * A step size of zero or less indicates no stepping.
     */
    int step() default 1;
}
