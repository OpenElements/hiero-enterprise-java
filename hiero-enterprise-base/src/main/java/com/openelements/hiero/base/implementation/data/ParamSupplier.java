package com.openelements.hiero.base.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;

/**
 * Interface for supplying parameters to a smart contract function parameter list. An implementation
 * can be used to add data in Java type {@code T} to a {@link ContractFunctionParameters} object.
 * Here the data will normally be converted to a native type that can be used in a smart contract
 * function call.
 *
 * @param <T>
 */
public interface ParamSupplier<T> {

  /**
   * Add the given value of Java type {@code T} as a parameter of the native type (see {@link
   * #getNativeType()}) to a {@link ContractFunctionParameters} object.
   *
   * @param value The value to add
   * @param params The parameters object to add the value to
   */
  void addParamToFunctionParameters(T value, ContractFunctionParameters params);

  /**
   * Check if the given value of Java type {@code T} is a valid parameter type for this supplier. A
   * valid type can be converted to the native type (see {@link #getNativeType()}) and added to a
   * {@link ContractFunctionParameters} object.
   *
   * @param value The value to check
   * @return {@code true} if the value is a valid parameter type, {@code false} otherwise
   */
  boolean isValidParam(T value);

  /**
   * Get the native type of the parameter that this supplier can add to a {@link
   * ContractFunctionParameters} object.
   *
   * @return The native type
   */
  String getNativeType();
}
