package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record AccountCreateRequest(
    Hbar maxTransactionFee, Duration transactionValidDuration, @NonNull Hbar initialBalance)
    implements TransactionRequest {

  public AccountCreateRequest {
    Objects.requireNonNull(maxTransactionFee, "maxTransactionFee is required");
    Objects.requireNonNull(transactionValidDuration, "transactionValidDuration is required");
    Objects.requireNonNull(initialBalance, "initialBalance is required");

    if (maxTransactionFee.toTinybars() < 0) {
      throw new IllegalArgumentException("maxTransactionFee must be non-negative");
    }
    if (transactionValidDuration.isNegative() || transactionValidDuration.isZero()) {
      throw new IllegalArgumentException("transactionValidDuration must be positive");
    }
    if (initialBalance.toTinybars() < 0) {
      throw new IllegalArgumentException("initialBalance must be non-negative");
    }
  }

  @NonNull
  public static AccountCreateRequest of(@NonNull Hbar initialBalance) {
    return new AccountCreateRequest(
        DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, initialBalance);
  }

  public static AccountCreateRequest of() {
    return of(Hbar.ZERO);
  }
}
