package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import org.jspecify.annotations.Nullable;

public record RoyaltyFee(
        long numeratorAmount,
        long denominatorAmount,
        long fallbackFeeAmount,
        @Nullable AccountId collectorAccountId,
        @Nullable TokenId denominatingTokenId
) {
}
