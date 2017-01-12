package com.arthanzel.theriverengine.common.ui.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that a field represents a file path and should be editable from a UI.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FileBinding {
    /**
     * If true, allows selecting only folders. If false, allows selecting only files.
     */
    boolean folders() default false;
}
