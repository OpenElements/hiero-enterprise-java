package com.openelements.hiero.base.solidity;

import com.openelements.hiero.base.data.ContractEventInstance;
import com.openelements.hiero.base.data.ContractEventInstance.ParameterInstance;
import com.openelements.hiero.base.data.ContractLog;
import com.openelements.hiero.smartcontract.abi.model.AbiEvent;
import com.openelements.hiero.smartcontract.abi.model.AbiParameter;
import com.openelements.hiero.smartcontract.abi.model.AbiParameterType;
import com.openelements.hiero.smartcontract.abi.util.HexConverter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

public class SolidityTools {

    private static Class<? extends Type> getMatchingTypeClass(
            AbiParameterType abiParameter) {
        return switch (abiParameter) {
            case AbiParameterType.ADDRESS -> Address.class;
            case STRING -> Utf8String.class;
            case BYTE32 -> Bytes32.class;
            case AbiParameterType.BOOL -> Bool.class;
            case UINT256 -> Uint256.class;
            case UINT -> Uint256.class;
            case TUPLE -> DynamicStruct.class;
            default -> throw new IllegalArgumentException("Unknown type: " + abiParameter);
        };
    }

    public static ContractEventInstance asEventInstance(final @NonNull ContractLog contractLog,
            final @NonNull AbiEvent event) {
        Objects.requireNonNull(contractLog, "contractLog must not be null");
        Objects.requireNonNull(event, "event must not be null");

        if (!event.anonymous()) {
            if (!contractLog.isEventOfType(event)) {
                throw new IllegalArgumentException("Event does not match log");
            }
        }
        //see https://github.com/web3/web3.js/blob/bf1691765bd9d4b0f7a4479e915207707d69226d/packages/web3-eth-abi/src/api/logs_api.ts#L74
        //see https://docs.soliditylang.org/en/latest/abi-spec.html#index-0
        int topicIndex = 0;
        if (!event.anonymous()) {
            topicIndex = 1;
        }

        final List<AbiParameter> indexedParameters = event.getIndexedInputParameters();
        final List<AbiParameter> nonIndexedParameters = event.getNonIndexedInputParameters();
        final Map<AbiParameter, ParameterInstance> mapping = new HashMap<>();

        for (AbiParameter parameter : indexedParameters) {
            mapping.put(parameter, new ParameterInstance(parameter.name(), parameter.type(),
                    contractLog.topics().get(topicIndex++).getBytes(StandardCharsets.UTF_8)));
        }

        final String data = contractLog.data().substring(2);
        int offset = 0;
        for (AbiParameter parameter : nonIndexedParameters) {
            final Class<? extends Type> web3jTypeClass = getMatchingTypeClass(parameter.type());
            final Type web3jType = TypeDecoder.decode(contractLog.data(), offset, web3jTypeClass);
            final int byteLength = web3jType.bytes32PaddedLength();
            final String substring = data.substring(offset, byteLength * 2);
            final byte[] value = HexConverter.hexToBytes(substring);
            mapping.put(parameter, new ParameterInstance(parameter.name(), parameter.type(), value));
            offset += byteLength * 2;
        }

        final List<ParameterInstance> parameters = new ArrayList<>();
        for (AbiParameter parameter : event.inputs()) {
            parameters.add(mapping.get(parameter));
        }
        return new ContractEventInstance(contractLog.contractId(), event.name(),
                Collections.unmodifiableList(parameters));
    }

    public static <T> T getValue(ParameterInstance parameterInstance) {
        final Class<? extends Type> web3jTypeClass = getMatchingTypeClass(parameterInstance.type());
        final String dataAsHex = HexConverter.bytesToHex(parameterInstance.value());
        final Type web3jType = TypeDecoder.decode(dataAsHex, web3jTypeClass);
        return (T) web3jType.getValue();
    }
}
