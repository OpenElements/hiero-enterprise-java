package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Represents an account of a Hiero network.
 *
 * @param accountId the ID of the account
 * @param publicKey the public key of the account
 * @param privateKey the private key of the account
 */
public record Account(
    @NonNull AccountId accountId, @NonNull PublicKey publicKey, @NonNull PrivateKey privateKey) {

  public Account {
    Objects.requireNonNull(accountId, "newAccountId must not be null");
    Objects.requireNonNull(publicKey, "publicKey must not be null");
    Objects.requireNonNull(privateKey, "privateKey must not be null");
  }

  /**
   * Creates an account object.
   *
   * @param accountId the ID of the account
   * @param publicKey the public key of the account
   * @param privateKey the private key of the account
   * @return the account
   */
  @NonNull
  public static Account of(
      @NonNull AccountId accountId, @NonNull PublicKey publicKey, @NonNull PrivateKey privateKey) {
    return new Account(accountId, publicKey, privateKey);
  }

  /**
   * Creates an account object.
   *
   * @param accountId the ID of the account
   * @param privateKey the private key of the account
   * @return the account
   */
  @NonNull
  public static Account of(@NonNull AccountId accountId, @NonNull PrivateKey privateKey) {
    Objects.requireNonNull(privateKey, "privateKey must not be null");
    return new Account(accountId, privateKey.getPublicKey(), privateKey);
  }
}
