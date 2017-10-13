package com.symbol.emdkallsamples;

/**
 * Created by Trudu Laurent on 06/10/2017.
 */

public enum eNavigationState {
    NONE(0),
    BYUSECASE(1),
    BYAPI(2),
    ALPHABETICALLY(3);

    private int _value;
    private static String[] mNavigation = new String[]{ "NONE", "By use case", "By Api", "Alphabetically"};

    eNavigationState(int Value) {
        this._value = Value;
    }

    public int getValue() {
        return _value;
    }

    public String getName(){
        return mNavigation[_value];
    }

    public static eNavigationState fromInt(int i) {
        for (eNavigationState b : eNavigationState.values()) {
            if (b.getValue() == i) { return b; }
        }
        return null;
    }

}
