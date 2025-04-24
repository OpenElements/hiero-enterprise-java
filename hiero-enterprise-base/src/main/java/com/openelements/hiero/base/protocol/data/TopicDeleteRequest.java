package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TopicId;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TopicDeleteRequest(@NonNull Hbar maxTransactionFee,
                                 @NonNull Duration transactionValidDuration,
                                 @NonNull PrivateKey adminKey,
                                 @NonNull TopicId topicId) implements TransactionRequest {

    public TopicDeleteRequest {
        Objects.requireNonNull(maxTransactionFee,"maxTransactionFee cannot be null");
        Objects.requireNonNull(transactionValidDuration, "transactionValidDuration cannot be null");
        Objects.requireNonNull(topicId, "topicId cannot be null");
    }

    public static TopicDeleteRequest of(@NonNull PrivateKey adminKey, @NonNull final TopicId topicId) {
        return new TopicDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, adminKey, topicId);
    }
}
