package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.common.util.TextUtils;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by martin on 2016-10-26.
 */
public class TextUtilsTest {
    @Test
    public void toWords() {
        assertEquals("Some Words Here", TextUtils.toWords("someWordsHere"));
        assertEquals("Some Words Here", TextUtils.toWords("SomeWordsHere"));
    }
}