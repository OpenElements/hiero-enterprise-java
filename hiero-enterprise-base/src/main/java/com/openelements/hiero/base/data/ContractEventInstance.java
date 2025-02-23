package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.smartcontract.abi.model.AbiParameterType;
import java.util.List;

public record ContractEventInstance(ContractId contractId, String eventName, List<ParameterInstance> parameters) {

    public record ParameterInstance(String name, AbiParameterType type, byte[] value) {
    }
}
