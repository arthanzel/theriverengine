/**
 * <p>
 * This package contains a selection of JavaFX components called "Field Editors" that
 * allow dynamic creation of UI controls from fields of Java objects. Fields
 * must be annotated with a binding annotation from the
 * {@link com.arthanzel.theriverengine.common.ui.binding} package that matches
 * the field's type.
 * </p>
 * <p>
 * <b>Important:</b> the <code>fe</code> package uses runtime reflection and
 * generics to dynamically create and bind controls to fields, and so is
 * <b>not</b> strictly type-safe. Always ensure that a field is annotated with a binding
 * of the appropriate type to avoid runtime exceptions.
 * </p>
 */
package com.arthanzel.theriverengine.common.ui.fe;