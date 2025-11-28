package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TokenTransferResult(@NonNull TransactionId transactionId, @NonNull Status status)
    implements TransactionResult {

  public TokenTransferResult {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    Objects.requireNonNull(status, "status must not be null");
  }
}
