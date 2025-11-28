package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.FileId;
import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record FileInfoResponse(
    @NonNull FileId fileId, int size, boolean deleted, Instant expirationTime) {

  public FileInfoResponse {
    Objects.requireNonNull(fileId, "fileId must not be null");
    if (size < 0) {
      throw new IllegalArgumentException("size must not be negative");
    }
  }
}
