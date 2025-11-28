package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Represents the result of an account balance query.
 *
 * @param hbars The balance of the account in hbars.
 */
public record AccountBalanceResponse(@NonNull Hbar hbars) {

  public AccountBalanceResponse {
    Objects.requireNonNull(hbars, "hbars must be non-null");
    if (hbars.toTinybars() < 0) {
      throw new IllegalArgumentException("hbars must be non-negative");
    }
  }

  public static AccountBalanceResponse of(Hbar hbars) {
    return new AccountBalanceResponse(hbars);
  }
}
