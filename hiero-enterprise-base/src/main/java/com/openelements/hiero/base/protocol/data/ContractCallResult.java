package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record ContractCallResult(@NonNull TransactionId transactionId, @NonNull Status status,
                                 @NonNull byte[] transactionHash, @NonNull Instant consensusTimestamp,
                                 @NonNull Hbar transactionFee,
                                 @NonNull ContractFunctionResult contractFunctionResult) implements TransactionRecord {

    public ContractCallResult {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(transactionHash, "transactionHash must not be null");
        Objects.requireNonNull(consensusTimestamp, "consensusTimestamp must not be null");
        Objects.requireNonNull(transactionFee, "transactionFee must not be null");
        Objects.requireNonNull(contractFunctionResult, "contractFunctionResult must not be null");

        if (transactionFee.toTinybars() < 0) {
            throw new IllegalArgumentException("transactionFee must not be negative");
        }
    }
}
