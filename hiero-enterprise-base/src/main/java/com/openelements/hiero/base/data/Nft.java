package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Represents a non-fungible token (NFT).
 *
 * @param tokenId the ID of the token type
 * @param serial the serial number of the NFT
 * @param owner the account that owns the NFT
 * @param metadata the metadata of the NFT
 */
public record Nft(
    @NonNull TokenId tokenId, long serial, @NonNull AccountId owner, @NonNull byte[] metadata) {

  public Nft {
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    Objects.requireNonNull(owner, "owner must not be null");
    Objects.requireNonNull(metadata, "metadata must not be null");
    if (serial < 0) {
      throw new IllegalArgumentException("serial must be greater than or equal to 0");
    }
  }
}
