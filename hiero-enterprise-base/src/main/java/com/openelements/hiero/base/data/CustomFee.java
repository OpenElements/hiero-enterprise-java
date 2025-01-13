package com.openelements.hiero.base.data;

import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public record CustomFee(
        @NonNull List<FixedFee> fixedFees,
        @NonNull List<FractionalFee> fractionalFees,
        @NonNull List<RoyaltyFee> royaltyFees
) {
    public CustomFee {
        Objects.requireNonNull(fixedFees, "fixedFees must not be null");
        Objects.requireNonNull(fractionalFees, "fractionalFees must not be null");
        Objects.requireNonNull(royaltyFees, "royaltyFees must not be null");
    }
}
