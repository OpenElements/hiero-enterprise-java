package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import org.jspecify.annotations.Nullable;

public record FractionalFee(
    long numeratorAmount,
    long denominatorAmount,
    @Nullable AccountId collectorAccountId,
    @Nullable TokenId denominatingTokenId) {
  public FractionalFee {
    if (numeratorAmount < 0) {
      throw new IllegalArgumentException("numeratorAmount must be greater than or equal to 0");
    }
    if (denominatorAmount < 0) {
      throw new IllegalArgumentException("denominatorAmount must be greater than or equal to 0");
    }
  }
}
