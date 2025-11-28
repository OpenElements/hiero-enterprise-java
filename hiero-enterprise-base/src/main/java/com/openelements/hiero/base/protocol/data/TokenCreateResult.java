package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TransactionId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TokenCreateResult(
    @NonNull TransactionId transactionId, @NonNull Status status, @NonNull TokenId tokenId)
    implements TransactionResult {

  public TokenCreateResult {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    Objects.requireNonNull(status, "status must not be null");
    Objects.requireNonNull(tokenId, "tokenId must not be null");
  }
}
