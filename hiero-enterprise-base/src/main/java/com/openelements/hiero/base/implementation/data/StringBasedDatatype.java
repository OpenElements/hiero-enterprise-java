package com.openelements.hiero.base.implementation.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.jspecify.annotations.NonNull;

public enum StringBasedDatatype implements ParamSupplier<String> {
  STRING("string", (v, params) -> params.addString(v)),
  BYTES("bytes", (v, params) -> addBytes(v, params)),
  BYTES32("bytes32", (v, params) -> addBytes32(v, params)),
  ADDRESS("address", (v, params) -> params.addAddress(v));

  private final String nativeType;

  private final BiConsumer<String, ContractFunctionParameters> addParam;

  StringBasedDatatype(
      final String nativeType, final BiConsumer<String, ContractFunctionParameters> addParam) {
    this.nativeType = nativeType;
    this.addParam = addParam;
  }

  public void addParamToFunctionParameters(
      final String value, @NonNull final ContractFunctionParameters params) {
    Objects.requireNonNull(params, "params must not be null");
    addParam.accept(value, params);
  }

  @Override
  public boolean isValidParam(@NonNull final String value) {
    Objects.requireNonNull(value, "value must not be null");
    if (this.equals(ADDRESS)) {
      try {
        AccountId.fromString(value);
      } catch (final Exception e) {
        throw new IllegalArgumentException("Invalid address", e);
      }
    }
    return true;
  }

  @Override
  public String getNativeType() {
    return nativeType;
  }

  private static void addBytes32(final String value, final ContractFunctionParameters params) {
    if (value == null) {
      throw new IllegalArgumentException("bytes32 value must not be null");
    }
    final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
    if (bytes.length > 32) {
      throw new IllegalArgumentException("bytes32 value must be 32 bytes or less");
    }
    params.addBytes32(bytes);
  }

  private static void addBytes(final String value, final ContractFunctionParameters params) {
    if (value == null) {
      throw new IllegalArgumentException("bytes32 value must not be null");
    }
    final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
    params.addBytes(bytes);
  }
}
