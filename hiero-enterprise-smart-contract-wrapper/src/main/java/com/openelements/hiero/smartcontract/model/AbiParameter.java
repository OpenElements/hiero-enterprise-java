package com.openelements.hiero.smartcontract.model;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record AbiParameter(@NonNull String name, @NonNull AbiParemeterType type, @NonNull List<AbiParameter> components,
                           boolean indexed) {

    public AbiParameter {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(components, "components");
    }

}
