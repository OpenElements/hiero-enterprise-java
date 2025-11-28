package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record AccountInfo(
    @NonNull AccountId accountId,
    @NonNull String evmAddress,
    long balance,
    long ethereumNonce,
    long pendingReward) {
  public AccountInfo {
    Objects.requireNonNull(accountId, "accountId must not be null");
    Objects.requireNonNull(evmAddress, "evmAddress must not be null");
  }
}
