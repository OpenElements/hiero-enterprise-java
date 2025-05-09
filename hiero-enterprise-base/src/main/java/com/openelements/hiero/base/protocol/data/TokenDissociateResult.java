package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record TokenDissociateResult(@NonNull TransactionId transactionId, @NonNull Status status) implements
        TransactionResult {
    public TokenDissociateResult {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }
}
