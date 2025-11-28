package com.openelements.hiero.base.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.jspecify.annotations.NonNull;

public enum BigIntegerBasedNumericDatatypes implements ParamSupplier<BigInteger> {
  INT72(
      "int72",
      (v, params) -> params.addInt72(v),
      BigInteger.valueOf(2).pow(71).negate(),
      BigInteger.valueOf(2).pow(71).subtract(BigInteger.ONE)),
  INT80(
      "int80",
      (v, params) -> params.addInt80(v),
      BigInteger.valueOf(2).pow(79).negate(),
      BigInteger.valueOf(2).pow(79).subtract(BigInteger.ONE)),
  INT88(
      "int88",
      (v, params) -> params.addInt88(v),
      BigInteger.valueOf(2).pow(87).negate(),
      BigInteger.valueOf(2).pow(87).subtract(BigInteger.ONE)),
  INT96(
      "int96",
      (v, params) -> params.addInt96(v),
      BigInteger.valueOf(2).pow(95).negate(),
      BigInteger.valueOf(2).pow(95).subtract(BigInteger.ONE)),
  INT104(
      "int104",
      (v, params) -> params.addInt104(v),
      BigInteger.valueOf(2).pow(103).negate(),
      BigInteger.valueOf(2).pow(103).subtract(BigInteger.ONE)),
  INT112(
      "int112",
      (v, params) -> params.addInt112(v),
      BigInteger.valueOf(2).pow(111).negate(),
      BigInteger.valueOf(2).pow(111).subtract(BigInteger.ONE)),
  INT120(
      "int120",
      (v, params) -> params.addInt120(v),
      BigInteger.valueOf(2).pow(119).negate(),
      BigInteger.valueOf(2).pow(119).subtract(BigInteger.ONE)),
  INT128(
      "int128",
      (v, params) -> params.addInt128(v),
      BigInteger.valueOf(2).pow(127).negate(),
      BigInteger.valueOf(2).pow(127).subtract(BigInteger.ONE)),
  INT136(
      "int136",
      (v, params) -> params.addInt136(v),
      BigInteger.valueOf(2).pow(135).negate(),
      BigInteger.valueOf(2).pow(135).subtract(BigInteger.ONE)),
  INT144(
      "int144",
      (v, params) -> params.addInt144(v),
      BigInteger.valueOf(2).pow(143).negate(),
      BigInteger.valueOf(2).pow(143).subtract(BigInteger.ONE)),
  INT152(
      "int152",
      (v, params) -> params.addInt152(v),
      BigInteger.valueOf(2).pow(151).negate(),
      BigInteger.valueOf(2).pow(151).subtract(BigInteger.ONE)),
  INT160(
      "int160",
      (v, params) -> params.addInt160(v),
      BigInteger.valueOf(2).pow(159).negate(),
      BigInteger.valueOf(2).pow(159).subtract(BigInteger.ONE)),
  INT168(
      "int168",
      (v, params) -> params.addInt168(v),
      BigInteger.valueOf(2).pow(167).negate(),
      BigInteger.valueOf(2).pow(167).subtract(BigInteger.ONE)),
  INT176(
      "int176",
      (v, params) -> params.addInt176(v),
      BigInteger.valueOf(2).pow(175).negate(),
      BigInteger.valueOf(2).pow(175).subtract(BigInteger.ONE)),
  INT184(
      "int184",
      (v, params) -> params.addInt184(v),
      BigInteger.valueOf(2).pow(183).negate(),
      BigInteger.valueOf(2).pow(183).subtract(BigInteger.ONE)),
  INT192(
      "int192",
      (v, params) -> params.addInt192(v),
      BigInteger.valueOf(2).pow(191).negate(),
      BigInteger.valueOf(2).pow(191).subtract(BigInteger.ONE)),
  INT200(
      "int200",
      (v, params) -> params.addInt200(v),
      BigInteger.valueOf(2).pow(199).negate(),
      BigInteger.valueOf(2).pow(199).subtract(BigInteger.ONE)),
  INT208(
      "int208",
      (v, params) -> params.addInt208(v),
      BigInteger.valueOf(2).pow(207).negate(),
      BigInteger.valueOf(2).pow(207).subtract(BigInteger.ONE)),
  INT216(
      "int216",
      (v, params) -> params.addInt216(v),
      BigInteger.valueOf(2).pow(215).negate(),
      BigInteger.valueOf(2).pow(215).subtract(BigInteger.ONE)),
  INT224(
      "int224",
      (v, params) -> params.addInt224(v),
      BigInteger.valueOf(2).pow(223).negate(),
      BigInteger.valueOf(2).pow(223).subtract(BigInteger.ONE)),
  INT232(
      "int232",
      (v, params) -> params.addInt232(v),
      BigInteger.valueOf(2).pow(231).negate(),
      BigInteger.valueOf(2).pow(231).subtract(BigInteger.ONE)),
  INT240(
      "int240",
      (v, params) -> params.addInt240(v),
      BigInteger.valueOf(2).pow(239).negate(),
      BigInteger.valueOf(2).pow(239).subtract(BigInteger.ONE)),
  INT248(
      "int248",
      (v, params) -> params.addInt248(v),
      BigInteger.valueOf(2).pow(247).negate(),
      BigInteger.valueOf(2).pow(247).subtract(BigInteger.ONE)),
  INT256(
      "int256",
      (v, params) -> params.addInt256(v),
      BigInteger.valueOf(2).pow(255).negate(),
      BigInteger.valueOf(2).pow(255).subtract(BigInteger.ONE)),

  UINT72(
      "uint72",
      (v, params) -> params.addUint72(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(72).subtract(BigInteger.ONE)),
  UINT80(
      "uint80",
      (v, params) -> params.addUint80(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(80).subtract(BigInteger.ONE)),
  UINT88(
      "uint88",
      (v, params) -> params.addUint88(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(88).subtract(BigInteger.ONE)),
  UINT96(
      "uint96",
      (v, params) -> params.addUint96(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(96).subtract(BigInteger.ONE)),
  UINT104(
      "uint104",
      (v, params) -> params.addUint104(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(104).subtract(BigInteger.ONE)),
  UINT112(
      "uint112",
      (v, params) -> params.addUint112(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(112).subtract(BigInteger.ONE)),
  UINT120(
      "uint120",
      (v, params) -> params.addUint120(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(120).subtract(BigInteger.ONE)),
  UINT128(
      "uint128",
      (v, params) -> params.addUint128(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(128).subtract(BigInteger.ONE)),
  UINT136(
      "uint136",
      (v, params) -> params.addUint136(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(136).subtract(BigInteger.ONE)),
  UINT144(
      "uint144",
      (v, params) -> params.addUint144(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(144).subtract(BigInteger.ONE)),
  UINT152(
      "uint152",
      (v, params) -> params.addUint152(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(152).subtract(BigInteger.ONE)),
  UINT160(
      "uint160",
      (v, params) -> params.addUint160(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(160).subtract(BigInteger.ONE)),
  UINT168(
      "uint168",
      (v, params) -> params.addUint168(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(168).subtract(BigInteger.ONE)),
  UINT176(
      "uint176",
      (v, params) -> params.addUint176(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(176).subtract(BigInteger.ONE)),
  UINT184(
      "uint184",
      (v, params) -> params.addUint184(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(184).subtract(BigInteger.ONE)),
  UINT192(
      "uint192",
      (v, params) -> params.addUint192(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(192).subtract(BigInteger.ONE)),
  UINT200(
      "uint200",
      (v, params) -> params.addUint200(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(200).subtract(BigInteger.ONE)),
  UINT208(
      "uint208",
      (v, params) -> params.addUint208(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(208).subtract(BigInteger.ONE)),
  UINT216(
      "uint216",
      (v, params) -> params.addUint216(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(216).subtract(BigInteger.ONE)),
  UINT224(
      "uint224",
      (v, params) -> params.addUint224(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(224).subtract(BigInteger.ONE)),
  UINT232(
      "uint232",
      (v, params) -> params.addUint232(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(232).subtract(BigInteger.ONE)),
  UINT240(
      "uint240",
      (v, params) -> params.addUint240(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(240).subtract(BigInteger.ONE)),
  UINT248(
      "uint248",
      (v, params) -> params.addUint248(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(248).subtract(BigInteger.ONE)),

  UINT256(
      "uint256",
      (v, params) -> params.addUint256(v),
      BigInteger.ZERO,
      BigInteger.valueOf(2).pow(256).subtract(BigInteger.ONE));
  private final BiConsumer<BigInteger, ContractFunctionParameters> addParam;

  private final BigInteger minValue;

  private final BigInteger maxValue;

  private final String nativeType;

  BigIntegerBasedNumericDatatypes(
      final String nativeType,
      BiConsumer<BigInteger, ContractFunctionParameters> addParam,
      BigInteger minValue,
      BigInteger maxValue) {
    this.nativeType = nativeType;
    this.addParam = addParam;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  @Override
  public void addParamToFunctionParameters(
      @NonNull final BigInteger value, final ContractFunctionParameters params) {
    Objects.requireNonNull(value, "value must not be null");
    if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
      throw new IllegalArgumentException("value out of range for type '" + this + "': " + value);
    }
    addParam.accept(value, params);
  }

  @Override
  public boolean isValidParam(final BigInteger value) {
    if (value == null) {
      return false;
    }
    return value.compareTo(minValue) >= 0 && value.compareTo(maxValue) <= 0;
  }

  @Override
  public String getNativeType() {
    return nativeType;
  }
}
