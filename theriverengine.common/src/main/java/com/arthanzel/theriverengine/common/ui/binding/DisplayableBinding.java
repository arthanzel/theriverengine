package com.arthanzel.theriverengine.common.ui.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that a field should have its value displayed in a UI.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayableBinding {
    /**
     * Indicates if the value should be displayed on a new line, below the label.
     */
    boolean newline() default false;
}
