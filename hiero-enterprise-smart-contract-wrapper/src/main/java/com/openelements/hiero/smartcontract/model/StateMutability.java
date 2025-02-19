package com.openelements.hiero.smartcontract.model;

public enum StateMutability {
    PURE,
    VIEW,
    PAYABLE,
    NONPAYABLE;

    public static StateMutability of(final String name) {
        return switch (name) {
            case "pure" -> PURE;
            case "view" -> VIEW;
            case "payable" -> PAYABLE;
            case "nonpayable" -> NONPAYABLE;
            default -> throw new IllegalArgumentException("Unknown state mutability: " + name);
        };
    }
}
