package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.smartcontract.abi.model.AbiParameterType;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record ContractEventInstance(@NonNull ContractId contractId, @Nullable String eventName,
                                    @NonNull List<ParameterInstance> parameters) {

    public ContractEventInstance {
        Objects.requireNonNull(contractId, "contractId must be provided");
        Objects.requireNonNull(parameters, "parameters must be provided");
    }

    public record ParameterInstance(@NonNull String name, @NonNull AbiParameterType type, @NonNull byte[] value) {

        public ParameterInstance {
            Objects.requireNonNull(name, "name must be provided");
            Objects.requireNonNull(type, "type must be provided");
            Objects.requireNonNull(value, "value must be provided");
        }
    }
}
