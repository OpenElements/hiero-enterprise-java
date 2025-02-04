package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.data.ContractParam;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record
ContractCallRequest(@NonNull Hbar maxTransactionFee,
                    @NonNull Duration transactionValidDuration,
                    @NonNull ContractId contractId,
                    @NonNull String functionName,
                    @NonNull List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public ContractCallRequest {
        Objects.requireNonNull(maxTransactionFee, "maxTransactionFee is required");
        Objects.requireNonNull(transactionValidDuration, "transactionValidDuration is required");
        Objects.requireNonNull(contractId, "contractId is required");
        Objects.requireNonNull(functionName, "functionName is required");
        Objects.requireNonNull(constructorParams, "constructorParams is required");
        if (maxTransactionFee.toTinybars() < 0) {
            throw new IllegalArgumentException("maxTransactionFee must be at >= null");
        }
        if (!transactionValidDuration.isPositive()) {
            throw new IllegalArgumentException("transactionValidDuration must be at >= zero");
        }
        if (functionName.isBlank() || functionName.contains(" ")) {
            throw new IllegalArgumentException("functionName must not be blank or contain spaces");
        }
    }

    @NonNull
    public static ContractCallRequest of(@NonNull String contractId, @NonNull String functionName,
            @Nullable ContractParam<?>... constructorParams) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId), functionName, constructorParams);
    }

    @NonNull
    public static ContractCallRequest of(@NonNull ContractId contractId, @NonNull String functionName,
            @Nullable ContractParam<?>... constructorParams) {
        if (constructorParams == null) {
            return of(contractId, functionName, List.of());
        } else {
            return of(contractId, functionName, List.of(constructorParams));
        }
    }

    @NonNull
    public static ContractCallRequest of(@NonNull String contractId, @NonNull String functionName,
            @NonNull List<ContractParam<?>> constructorParams) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId), functionName, constructorParams);
    }

    @NonNull
    public static ContractCallRequest of(@NonNull ContractId contractId, @NonNull String functionName,
            @NonNull List<ContractParam<?>> constructorParams) {
        return new ContractCallRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contractId,
                functionName, List.copyOf(constructorParams));
    }
}
