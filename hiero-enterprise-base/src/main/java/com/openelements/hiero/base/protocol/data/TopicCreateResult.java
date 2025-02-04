package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TopicCreateResult(@NonNull TransactionId transactionId, @NonNull Status status,
                                @NonNull TopicId fileId) implements TransactionResult {

    public TopicCreateResult {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(fileId, "fileId must not be null");
    }
}
