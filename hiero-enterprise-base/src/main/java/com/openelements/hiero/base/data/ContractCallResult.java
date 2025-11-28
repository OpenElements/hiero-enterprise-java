package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.Hbar;
import java.math.BigInteger;

/**
 * Interface for the result of a contract call. This is used to get the return values of a contract
 * call. The return values are indexed from 0 to n-1.
 */
public interface ContractCallResult {

  /**
   * Get the amount of gas used by the contract call.
   *
   * @return the amount of gas used by the contract call.
   */
  long gasUsed();

  /**
   * Get the amount of hbar used by the contract call.
   *
   * @return the amount of hbar used by the contract call.
   */
  Hbar cost();

  /**
   * Get a return value as a string.
   *
   * @param index the index of the return value.
   * @return the return value as a string.
   */
  String getString(int index);

  /**
   * Get a return value as an address.
   *
   * @param index the index of the return value.
   * @return the return value as an address.
   */
  String getAddress(int index);

  /**
   * Get a return value as a boolean.
   *
   * @param index the index of the return value.
   * @return the return value as a boolean.
   */
  boolean getBool(int index);

  /**
   * Get a return value as an int8.
   *
   * @param index the index of the return value.
   * @return the return value as an int8.
   */
  byte getInt8(int index);

  /**
   * Get a return value as an int32.
   *
   * @param index the index of the return value.
   * @return the return value as an int32.
   */
  int getInt32(int index);

  /**
   * Get a return value as an int64.
   *
   * @param index the index of the return value.
   * @return the return value as an int64.
   */
  long getInt64(int index);

  /**
   * Get a return value as an int256.
   *
   * @param index the index of the return value.
   * @return the return value as an int256.
   */
  BigInteger getInt256(int index);

  /**
   * Get a return value as an uint8.
   *
   * @param index the index of the return value.
   * @return the return value as an uint8.
   */
  long getUint8(int index);

  /**
   * Get a return value as an uint32.
   *
   * @param index the index of the return value.
   * @return the return value as an uint32.
   */
  long getUint32(int index);

  /**
   * Get a return value as an uint64.
   *
   * @param index the index of the return value.
   * @return the return value as an uint64.
   */
  long getUint64(int index);

  /**
   * Get a return value as an uint256.
   *
   * @param index the index of the return value.
   * @return the return value as an uint256.
   */
  BigInteger getUint256(int index);
}
