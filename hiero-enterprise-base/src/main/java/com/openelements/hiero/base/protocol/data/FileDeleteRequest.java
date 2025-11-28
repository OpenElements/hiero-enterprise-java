package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record FileDeleteRequest(
    @NonNull Hbar maxTransactionFee,
    @NonNull Duration transactionValidDuration,
    @NonNull FileId fileId)
    implements TransactionRequest {

  public FileDeleteRequest {
    Objects.requireNonNull(fileId, "fileId must not be null");
  }

  @NonNull
  public static FileDeleteRequest of(@NonNull String fileId) {
    Objects.requireNonNull(fileId, "fileId must not be null");
    return of(FileId.fromString(fileId));
  }

  @NonNull
  public static FileDeleteRequest of(@NonNull FileId fileId) {
    return new FileDeleteRequest(
        DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId);
  }
}
