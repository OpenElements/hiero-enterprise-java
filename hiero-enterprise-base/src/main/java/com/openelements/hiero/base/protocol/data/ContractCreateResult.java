package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record ContractCreateResult(@NonNull TransactionId transactionId, @NonNull Status status,
                                   @NonNull ContractId contractId) implements TransactionResult {

    public ContractCreateResult {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(contractId, "contractId must not be null");
    }
}
