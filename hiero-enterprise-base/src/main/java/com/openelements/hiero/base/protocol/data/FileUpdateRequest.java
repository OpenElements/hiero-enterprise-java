package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;
import java.time.Instant;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record FileUpdateRequest(
    @NonNull Hbar maxTransactionFee,
    @NonNull Duration transactionValidDuration,
    @NonNull FileId fileId,
    @Nullable byte[] contents,
    @Nullable Instant expirationTime,
    @Nullable String fileMemo)
    implements TransactionRequest {

  private static final String DEFAULT_FILE_MEMO = "";

  public static final int FILE_CREATE_MAX_BYTES = 2048;

  public FileUpdateRequest {
    if (contents != null && contents.length > FILE_CREATE_MAX_BYTES) {
      throw new IllegalArgumentException(
          "File contents must be less than " + FILE_CREATE_MAX_BYTES + " bytes");
    }
  }

  public static FileUpdateRequest of(@NonNull FileId fileId, @NonNull byte[] contents) {
    return new FileUpdateRequest(
        DEFAULT_MAX_TRANSACTION_FEE,
        DEFAULT_TRANSACTION_VALID_DURATION,
        fileId,
        contents,
        null,
        DEFAULT_FILE_MEMO);
  }

  public static FileUpdateRequest of(@NonNull FileId fileId, @NonNull Instant expirationTime) {
    return new FileUpdateRequest(
        DEFAULT_MAX_TRANSACTION_FEE,
        DEFAULT_TRANSACTION_VALID_DURATION,
        fileId,
        null,
        expirationTime,
        DEFAULT_FILE_MEMO);
  }
}
