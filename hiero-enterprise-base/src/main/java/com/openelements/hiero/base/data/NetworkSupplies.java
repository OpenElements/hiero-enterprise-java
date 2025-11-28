package com.openelements.hiero.base.data;

import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record NetworkSupplies(@NonNull String releasedSupply, @NonNull String totalSupply) {
  public NetworkSupplies {
    Objects.requireNonNull(releasedSupply, "releasedSupply must not be null");
    Objects.requireNonNull(totalSupply, "totalSupply must not be null");
  }
}
