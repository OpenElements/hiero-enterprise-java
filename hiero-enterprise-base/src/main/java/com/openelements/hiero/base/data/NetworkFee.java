package com.openelements.hiero.base.data;

import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record NetworkFee(long gas, @NonNull String transactionType) {
  public NetworkFee {
    Objects.requireNonNull(transactionType, "transactionType must not be null");
  }
}
