package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record AccountDeleteResult(
    @NonNull TransactionId transactionId,
    @NonNull Status status,
    @NonNull byte[] transactionHash,
    Instant consensusTimestamp,
    Hbar transactionFee)
    implements TransactionRecord {

  public AccountDeleteResult {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    Objects.requireNonNull(status, "status must not be null");
    Objects.requireNonNull(transactionHash, "transactionHash must not be null");
  }
}
