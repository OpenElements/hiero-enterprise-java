package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.TransactionId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record ChunkInfo(
    @NonNull TransactionId initialTransactionId,
    int nonce,
    int number,
    int total,
    boolean scheduled) {
  public ChunkInfo {
    Objects.requireNonNull(initialTransactionId, "initialTransactionId must not be null");
  }
}
