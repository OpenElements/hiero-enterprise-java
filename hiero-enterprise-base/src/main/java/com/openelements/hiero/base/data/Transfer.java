package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record Transfer(@NonNull AccountId account, long amount, boolean isApproval) {
  public Transfer {
    Objects.requireNonNull(account, "account cannot be null");
  }
}
