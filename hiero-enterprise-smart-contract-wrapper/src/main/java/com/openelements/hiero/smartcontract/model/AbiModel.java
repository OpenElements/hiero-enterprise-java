package com.openelements.hiero.smartcontract.model;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record AbiModel(@NonNull List<AbiEntry> entries) {

    public AbiModel {
        Objects.requireNonNull(entries, "entries");
    }
}
