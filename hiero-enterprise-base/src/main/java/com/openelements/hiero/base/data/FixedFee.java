package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import org.jspecify.annotations.Nullable;

public record FixedFee(long amount, @Nullable AccountId collectorAccountId, @Nullable TokenId denominatingTokenId) {
}
