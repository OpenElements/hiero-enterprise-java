package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TokenDissociateRequest(
    @NonNull Hbar maxTransactionFee,
    @NonNull Duration transactionValidDuration,
    @NonNull List<TokenId> tokenIds,
    @NonNull AccountId accountId,
    @NonNull PrivateKey accountKey)
    implements TransactionRequest {
  public TokenDissociateRequest {
    Objects.requireNonNull(tokenIds, "tokenIds must not be null");
    Objects.requireNonNull(accountId, "accountId must not be null");
    Objects.requireNonNull(accountKey, "accountKey must not be null");
  }

  public static TokenDissociateRequest of(
      @NonNull TokenId tokenId, @NonNull AccountId accountId, @NonNull PrivateKey accountKey) {
    return of(List.of(tokenId), accountId, accountKey);
  }

  public static TokenDissociateRequest of(
      @NonNull List<TokenId> tokenIds,
      @NonNull AccountId accountId,
      @NonNull PrivateKey accountKey) {
    return new TokenDissociateRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        tokenIds,
        accountId,
        accountKey);
  }
}
