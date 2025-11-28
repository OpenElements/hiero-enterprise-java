package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TopicId;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TopicUpdateRequest(
    @NonNull Hbar maxTransactionFee,
    @NonNull Duration transactionValidDuration,
    @NonNull TopicId topicId,
    @NonNull PrivateKey adminKey,
    @Nullable PrivateKey updatedAdminKey,
    @Nullable PrivateKey submitKey,
    @Nullable String memo)
    implements TransactionRequest {
  public TopicUpdateRequest {
    Objects.requireNonNull(maxTransactionFee, "maxTransactionFee cannot be null");
    Objects.requireNonNull(transactionValidDuration, "transactionValidDuration cannot be null");
    Objects.requireNonNull(topicId, "topicId cannot be null");
    Objects.requireNonNull(adminKey, "adminKey cannot be null");
  }

  public static TopicUpdateRequest of(TopicId topicId, PrivateKey adminKey, String memo) {
    return of(topicId, adminKey, null, null, memo);
  }

  public static TopicUpdateRequest of(
      TopicId topicId, PrivateKey adminKey, PrivateKey updatedAdminKey, PrivateKey submitKey) {
    return of(topicId, adminKey, updatedAdminKey, submitKey, null);
  }

  public static TopicUpdateRequest updateAdminKey(
      TopicId topicId, PrivateKey adminKey, PrivateKey updatedAdminKey) {
    return of(topicId, adminKey, updatedAdminKey, null, null);
  }

  public static TopicUpdateRequest updateSubmitKey(
      TopicId topicId, PrivateKey adminKey, PrivateKey submitKey) {
    return of(topicId, adminKey, null, submitKey, null);
  }

  public static TopicUpdateRequest of(
      TopicId topicId,
      PrivateKey adminKey,
      PrivateKey updatedAdminKey,
      PrivateKey submitKey,
      String memo) {
    return new TopicUpdateRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        topicId,
        adminKey,
        updatedAdminKey,
        submitKey,
        memo);
  }
}
