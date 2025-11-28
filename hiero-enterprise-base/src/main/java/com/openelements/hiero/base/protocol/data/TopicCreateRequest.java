package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TopicCreateRequest(
    @NonNull Hbar maxTransactionFee,
    @NonNull Duration transactionValidDuration,
    @NonNull PrivateKey adminKey,
    @Nullable PrivateKey submitKey,
    @Nullable String memo)
    implements TransactionRequest {

  public TopicCreateRequest {
    Objects.requireNonNull(maxTransactionFee, "maxTransactionFee cannot be null");
    Objects.requireNonNull(transactionValidDuration, "transactionValidDuration cannot be null");
    Objects.requireNonNull(adminKey, "adminKey cannot be null");
  }

  public static TopicCreateRequest of(PrivateKey adminKey) {
    return of(adminKey, null, null);
  }

  public static TopicCreateRequest of(PrivateKey adminKey, String memo) {
    return of(adminKey, null, memo);
  }

  public static TopicCreateRequest of(PrivateKey adminKey, PrivateKey submitKey) {
    return of(adminKey, submitKey, null);
  }

  public static TopicCreateRequest of(PrivateKey adminKey, PrivateKey submitKey, String memo) {
    return new TopicCreateRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        adminKey,
        submitKey,
        memo);
  }
}
