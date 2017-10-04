package com.symbol.emdkallsamples;

/**
 * Created by Trudu Laurent on 18/09/2017.
 */

public enum eAPIEnums {
    NONE(-1),
    EMDK(0),
    PROFILEMANAGER(1),
    SIMULSCAN(2),
    DATAWEDGE(3),
    ANDROID(4),
    MX(5);

    private int _value;
    private static String[] mAPIs = new String[]{"EMDK", "ProfileManager", "Simulscan", "DataWedge", "Android", "MX"};

    eAPIEnums(int Value) {
        this._value = Value;
    }

    public int getValue() {
        return _value;
    }

    public String getName(){
        return mAPIs[_value];
    }

    public static eAPIEnums fromInt(int i) {
        for (eAPIEnums b : eAPIEnums.values()) {
            if (b.getValue() == i) { return b; }
        }
        return null;
    }
}
