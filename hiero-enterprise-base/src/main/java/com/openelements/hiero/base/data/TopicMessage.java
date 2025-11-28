package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TopicId;
import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TopicMessage(
    @Nullable ChunkInfo chunkInfo,
    @NonNull Instant consensusTimestamp,
    @NonNull String message,
    @NonNull AccountId payerAccountId,
    byte[] runningHash,
    int runningHashVersion,
    long sequenceNumber,
    @NonNull TopicId topicId) {
  public TopicMessage {
    Objects.requireNonNull(consensusTimestamp, "consensusTimestamp must not be null");
    Objects.requireNonNull(message, "message must not be null");
    Objects.requireNonNull(payerAccountId, "payerAccountId must not be null");
    Objects.requireNonNull(topicId, "topicId must not be null");
  }
}
