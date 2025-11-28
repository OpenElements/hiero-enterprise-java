package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import org.jspecify.annotations.Nullable;

public record RoyaltyFee(
    long numeratorAmount,
    long denominatorAmount,
    long fallbackFeeAmount,
    @Nullable AccountId collectorAccountId,
    @Nullable TokenId denominatingTokenId) {
  public RoyaltyFee {
    if (numeratorAmount < 0) {
      throw new IllegalArgumentException("numeratorAmount must be greater than or equal to 0");
    }
    if (denominatorAmount < 0) {
      throw new IllegalArgumentException("denominatorAmount must be greater than or equal to 0");
    }
  }
}
