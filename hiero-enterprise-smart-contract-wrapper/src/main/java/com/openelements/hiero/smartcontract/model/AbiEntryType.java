package com.openelements.hiero.smartcontract.model;

public enum AbiEntryType {
    FUNCTION,
    EVENT,
    CONSTRUCTOR,
    RECEIVE,
    FALLBACK;

    public static AbiEntryType of(String name) {
        if (name.equals("function")) {
            return FUNCTION;
        }
        if (name.equals("event")) {
            return EVENT;
        }
        if (name.equals("constructor")) {
            return CONSTRUCTOR;
        }
        if (name.equals("receive")) {
            return RECEIVE;
        }
        if (name.equals("fallback")) {
            return FALLBACK;
        }
        throw new IllegalArgumentException("Unknown ABI entry type: " + name);
    }

    public boolean isCompatibleWithFunction() {
        if (this == FUNCTION) {
            return true;
        }
        if (this == RECEIVE) {
            return true;
        }
        if (this == FALLBACK) {
            return true;
        }
        if (this == CONSTRUCTOR) {
            return true;
        }
        return false;
    }
}
