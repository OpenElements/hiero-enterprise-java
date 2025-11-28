package com.openelements.hiero.base.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.jspecify.annotations.NonNull;

public enum LongBasedNumericDatatypes implements ParamSupplier<Long> {
  INT8("int8", (v, params) -> params.addInt8(v.byteValue()), Byte.MIN_VALUE, Byte.MAX_VALUE),
  UINT8("uint8", (v, params) -> params.addUint8(v.byteValue()), 0L, 255L),
  INT16("int16", (v, params) -> params.addInt16(v.shortValue()), Short.MIN_VALUE, Short.MAX_VALUE),
  UINT16("uint16", (v, params) -> params.addUint16(v.shortValue()), 0L, 65535L),
  INT32(
      "int32", (v, params) -> params.addInt32(v.intValue()), Integer.MIN_VALUE, Integer.MAX_VALUE),
  UINT32("uint32", (v, params) -> params.addUint32(v.intValue()), 0L, 4294967295L),
  INT40("int40", (v, params) -> params.addInt40(v), -549755813888L, 549755813887L),
  UINT40("uint40", (v, params) -> params.addUint40(v), 0L, 1099511627775L),
  INT48("int48", (v, params) -> params.addInt48(v), -140737488355328L, 140737488355327L),
  UINT48("uint48", (v, params) -> params.addUint48(v), 0L, 281474976710655L),
  INT56("int56", (v, params) -> params.addInt56(v), -72057594037927936L, 72057594037927935L),
  UINT56("uint56", (v, params) -> params.addUint56(v), 0L, 144115188075855871L),
  INT64("int64", (v, params) -> params.addInt64(v), Long.MIN_VALUE, Long.MAX_VALUE);
  // TODO; UINT64 but max value is > long max value

  private final BiConsumer<Long, ContractFunctionParameters> addParam;

  private final long minValue;

  private final long maxValue;

  private final String nativeType;

  LongBasedNumericDatatypes(
      final String nativeType,
      final BiConsumer<Long, ContractFunctionParameters> addParam,
      long minValue,
      long maxValue) {
    this.nativeType = nativeType;
    this.addParam = addParam;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  @Override
  public void addParamToFunctionParameters(
      @NonNull final Long value, final ContractFunctionParameters params) {
    Objects.requireNonNull(value, "value must not be null");
    addParam.accept(value.longValue(), params);
  }

  @Override
  public boolean isValidParam(final Long value) {
    if (value == null) {
      return false;
    }
    return value >= minValue && value <= maxValue;
  }

  @Override
  public String getNativeType() {
    return nativeType;
  }

  public void addParam(final long value, @NonNull final ContractFunctionParameters params) {
    Objects.requireNonNull(params, "params must not be null");
    if (value < minValue || value > maxValue) {
      throw new IllegalArgumentException("value out of range for type '" + this + "': " + value);
    }
    addParam.accept(value, params);
  }
}
