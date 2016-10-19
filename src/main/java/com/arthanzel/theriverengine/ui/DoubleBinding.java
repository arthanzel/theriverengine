package com.arthanzel.theriverengine.ui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleBinding {
    double min() default 0;
    double max() default 10;
}
