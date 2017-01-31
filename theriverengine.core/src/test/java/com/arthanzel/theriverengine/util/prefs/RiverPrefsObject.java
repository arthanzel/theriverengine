package com.arthanzel.theriverengine.util.prefs;

public class RiverPrefsObject {
    private String stringObj = "foo";
    private double doubleObj = 1;
    private int intObj = 1;
    private boolean booleanObj = false;

    public String getStringObj() {
        return stringObj;
    }

    public void setStringObj(String stringObj) {
        this.stringObj = stringObj;
    }

    public double getDoubleObj() {
        return doubleObj;
    }

    public void setDoubleObj(double doubleObj) {
        this.doubleObj = doubleObj;
    }

    public int getIntObj() {
        return intObj;
    }

    public void setIntObj(int intObj) {
        this.intObj = intObj;
    }

    public boolean isBooleanObj() {
        return booleanObj;
    }

    public void setBooleanObj(boolean booleanObj) {
        this.booleanObj = booleanObj;
    }
}
