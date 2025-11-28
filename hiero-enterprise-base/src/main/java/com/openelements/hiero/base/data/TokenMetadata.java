package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;

public interface TokenMetadata {

  TokenId tokenId();

  String name();

  String symbol();

  AccountId treasuryAccountId();
}
