package com.openelements.hiero.base.data;

import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record ExchangeRates(@NonNull ExchangeRate currentRate, @NonNull ExchangeRate nextRate) {
  public ExchangeRates {
    Objects.requireNonNull(currentRate, "currentRate must not be null");
    Objects.requireNonNull(nextRate, "nextRate must not be null");
  }
}
