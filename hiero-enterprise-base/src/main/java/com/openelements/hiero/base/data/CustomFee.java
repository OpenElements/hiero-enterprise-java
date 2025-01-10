package com.openelements.hiero.base.data;

import java.util.List;

public record CustomFee(List<FixedFee> fixedFees, List<FractionalFee> fractionalFees, List<RoyaltyFee> royaltyFees) {
}
