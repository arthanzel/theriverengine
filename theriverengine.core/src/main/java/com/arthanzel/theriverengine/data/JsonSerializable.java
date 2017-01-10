package com.arthanzel.theriverengine.data;

import com.google.gson.JsonObject;

/**
 * JsonSerializable is an object that can serialize itself into JavaScript Object Notation (JSON) format.
 */
public interface JsonSerializable {
    JsonObject toJson();
}
