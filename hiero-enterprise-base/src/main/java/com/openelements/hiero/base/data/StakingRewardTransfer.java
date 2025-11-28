package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record StakingRewardTransfer(@NonNull AccountId account, long amount) {
  public StakingRewardTransfer {
    Objects.requireNonNull(account, "account cannot be null");
  }
}
