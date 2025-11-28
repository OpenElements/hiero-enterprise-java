package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import java.util.Objects;

public record NftMetadata(TokenId tokenId, String name, String symbol, AccountId treasuryAccountId)
    implements TokenMetadata {

  public NftMetadata {
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    Objects.requireNonNull(name, "name must not be null");
    Objects.requireNonNull(symbol, "symbol must not be null");
    Objects.requireNonNull(treasuryAccountId, "treasuryAccountId must not be null");
  }
}
