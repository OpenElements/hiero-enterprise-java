package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record Balance(@NonNull AccountId accountId, long balance, long decimals) {
  public Balance {
    Objects.requireNonNull(accountId, "accountId must not be null");
  }
}
