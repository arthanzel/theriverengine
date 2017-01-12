package com.arthanzel.theriverengine.common.ui.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that a field should be editable in UIs as a numeric value using
 * a slider control.
 *
 * @author Martin
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SliderBinding {
    /**
     * Defines the minimum value imposed by the slider.
     */
    double min();

    /**
     * Defines the maximum value imposed by the slider.
     */
    double max();

    /**
     * Defines the step size imposed by the slider.
     * A step size of zero or less indicates no stepping.
     */
    double step() default 0;
}
