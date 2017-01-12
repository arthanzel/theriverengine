package com.arthanzel.theriverengine.common.ui.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * BindingName attaches a custom string to a field or object. If present, this
 * string is displayed in UIs instead of a name automatically generated from
 * the field name or object's class name.
 *
 * @author Martin
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BindingName {
    String value();
}
