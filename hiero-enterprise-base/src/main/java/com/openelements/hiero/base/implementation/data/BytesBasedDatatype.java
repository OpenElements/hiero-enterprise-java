package com.openelements.hiero.base.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.openelements.hiero.base.data.Bytes;
import java.util.Objects;
import java.util.function.BiConsumer;

public enum BytesBasedDatatype implements ParamSupplier<Bytes> {

    BYTES("bytes", (v, params) -> params.addBytes(v.bytes())),
    BYTES32("bytes32", (v, params) -> params.addBytes(v.bytes()));

    private final String nativeType;

    private final BiConsumer<Bytes, ContractFunctionParameters> addParam;

    BytesBasedDatatype(final String nativeType, final BiConsumer<Bytes, ContractFunctionParameters> addParam) {
        this.nativeType = nativeType;
        this.addParam = addParam;
    }

    @Override
    public void addParamToFunctionParameters(Bytes value, ContractFunctionParameters params) {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(params, "params must not be null");
        addParam.accept(value, params);
    }

    @Override
    public boolean isValidParam(Bytes value) {
        if (value == null) {
            return false;
        }
        if (value.bytes().length > 32 && this.equals(BYTES32)) {
            return false;
        }
        return true;
    }

    @Override
    public String getNativeType() {
        return nativeType;
    }
}
