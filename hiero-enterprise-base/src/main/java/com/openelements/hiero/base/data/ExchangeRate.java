package com.openelements.hiero.base.data;

import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record ExchangeRate(
    int centEquivalent, int hbarEquivalent, @NonNull Instant expirationTime) {
  public ExchangeRate {
    Objects.requireNonNull(expirationTime, "expirationTime must not be null");
  }
}
