package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record Token(
    long decimals,
    byte[] metadata,
    @NonNull String name,
    @NonNull String symbol,
    @Nullable TokenId tokenId,
    @NonNull TokenType type) {
  public Token {
    Objects.requireNonNull(type, "type must not be null");
    Objects.requireNonNull(name, "name must not be null");
    Objects.requireNonNull(symbol, "symbol must not be null");
    Objects.requireNonNull(metadata, "metadata must not be null");
  }
}
