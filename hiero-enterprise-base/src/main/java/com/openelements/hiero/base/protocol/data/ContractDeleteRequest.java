package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record ContractDeleteRequest(@NonNull Hbar maxTransactionFee,
                                    @NonNull Duration transactionValidDuration,
                                    @NonNull ContractId contractId,
                                    @Nullable ContractId transferFeeToContractId,
                                    @Nullable AccountId transferFeeToAccountId) implements TransactionRequest {

    public ContractDeleteRequest {
        Objects.requireNonNull(maxTransactionFee, "maxTransactionFee is required");
        Objects.requireNonNull(transactionValidDuration, "transactionValidDuration is required");
        Objects.requireNonNull(contractId, "contractId is required");
        if (maxTransactionFee.toTinybars() < 0) {
            throw new IllegalArgumentException("maxTransactionFee must be non-negative");
        }
        if (transactionValidDuration.isNegative() || transactionValidDuration.isZero()) {
            throw new IllegalArgumentException("transactionValidDuration must be positive");
        }
    }

    @NonNull
    public static ContractDeleteRequest of(@NonNull String contractId) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId));
    }

    @NonNull
    public static ContractDeleteRequest of(@NonNull ContractId contractId) {
        return new ContractDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contractId,
                null, null);
    }
}
