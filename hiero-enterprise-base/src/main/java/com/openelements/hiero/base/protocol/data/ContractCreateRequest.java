package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.data.ContractParam;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record ContractCreateRequest(@NonNull Hbar maxTransactionFee,
                                    @NonNull Duration transactionValidDuration,
                                    @NonNull FileId fileId,
                                    @NonNull List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public ContractCreateRequest {
        Objects.requireNonNull(maxTransactionFee, "maxTransactionFee is required");
        Objects.requireNonNull(transactionValidDuration, "transactionValidDuration is required");
        Objects.requireNonNull(fileId, "fileId is required");
        Objects.requireNonNull(constructorParams, "constructorParams is required");
        if (maxTransactionFee.toTinybars() < 0) {
            throw new IllegalArgumentException("maxTransactionFee must be non-negative");
        }
        if (transactionValidDuration.isNegative() || transactionValidDuration.isZero()) {
            throw new IllegalArgumentException("transactionValidDuration must be positive");
        }
    }

    @NonNull
    public static ContractCreateRequest of(@NonNull String fileId, @Nullable ContractParam<?>... constructorParams) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId), constructorParams);
    }

    @NonNull
    public static ContractCreateRequest of(@NonNull FileId fileId, @Nullable ContractParam<?>... constructorParams) {
        if (constructorParams == null) {
            return of(fileId, List.of());
        } else {
            return of(fileId, List.of(constructorParams));
        }
    }

    @NonNull
    public static ContractCreateRequest of(@NonNull String fileId, @NonNull List<ContractParam<?>> constructorParams) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId), constructorParams);
    }

    @NonNull
    public static ContractCreateRequest of(@NonNull FileId fileId, @NonNull List<ContractParam<?>> constructorParams) {
        return new ContractCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId,
                List.copyOf(constructorParams));
    }
}
