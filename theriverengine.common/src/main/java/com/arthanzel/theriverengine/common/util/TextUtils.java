package com.arthanzel.theriverengine.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Provides helper methods for dealing with text.
 *
 * @author Martin
 */
public class TextUtils {
    /**
     * Splits a camel-cased string into individually-capitalized words. Example: someCamelCase -> Some Camel Case.
     * @param str A String.
     * @return String with individually-capitalized words.
     */
    public static String toWords(String str) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(str);
        return StringUtils.capitalize(String.join(" ", (CharSequence[]) words));
    }
}
