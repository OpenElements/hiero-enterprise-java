package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Balance;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Token;
import com.openelements.hiero.base.data.TokenInfo;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.mirrornode.TokenRepository;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class TokenRepositoryImpl implements TokenRepository {
  private final MirrorNodeClient mirrorNodeClient;

  public TokenRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
    this.mirrorNodeClient =
        Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
  }

  @Override
  public Page<Token> findByAccount(@NonNull AccountId accountId) throws HieroException {
    return mirrorNodeClient.queryTokensForAccount(accountId);
  }

  @Override
  public Optional<TokenInfo> findById(@NonNull TokenId tokenId) throws HieroException {
    return mirrorNodeClient.queryTokenById(tokenId);
  }

  @Override
  public Page<Balance> getBalances(@NonNull TokenId tokenId) throws HieroException {
    return mirrorNodeClient.queryTokenBalances(tokenId);
  }

  @Override
  public Page<Balance> getBalancesForAccount(@NonNull TokenId tokenId, @NonNull AccountId accountId)
      throws HieroException {
    return mirrorNodeClient.queryTokenBalancesForAccount(tokenId, accountId);
  }
}
