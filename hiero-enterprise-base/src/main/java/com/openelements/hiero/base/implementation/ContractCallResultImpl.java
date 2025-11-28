package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.data.ContractCallResult;
import java.math.BigInteger;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class ContractCallResultImpl implements ContractCallResult {

  private final ContractFunctionResult innerResult;

  public ContractCallResultImpl(@NonNull final ContractFunctionResult innerResult) {
    this.innerResult = Objects.requireNonNull(innerResult, "innerResult must not be null");
  }

  @Override
  public long gasUsed() {
    return innerResult.gasUsed;
  }

  @Override
  public Hbar cost() {
    return innerResult.hbarAmount;
  }

  @Override
  public String getString(int index) {
    return innerResult.getString(index);
  }

  @Override
  public String getAddress(int index) {
    return innerResult.getAddress(index);
  }

  @Override
  public boolean getBool(int index) {
    return innerResult.getBool(index);
  }

  @Override
  public byte getInt8(int index) {
    return innerResult.getInt8(index);
  }

  @Override
  public int getInt32(int index) {
    return innerResult.getInt32(index);
  }

  @Override
  public long getInt64(int index) {
    return innerResult.getInt64(index);
  }

  @Override
  public BigInteger getInt256(int index) {
    return innerResult.getInt256(index);
  }

  @Override
  public long getUint8(int index) {
    return innerResult.getUint8(index);
  }

  @Override
  public long getUint32(int index) {
    return innerResult.getUint32(index);
  }

  @Override
  public long getUint64(int index) {
    return innerResult.getUint64(index);
  }

  @Override
  public BigInteger getUint256(int index) {
    return innerResult.getUint256(index);
  }
}
