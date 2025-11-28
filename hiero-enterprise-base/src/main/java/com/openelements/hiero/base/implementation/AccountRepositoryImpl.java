package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.mirrornode.AccountRepository;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class AccountRepositoryImpl implements AccountRepository {
  private final MirrorNodeClient mirrorNodeClient;

  public AccountRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
    this.mirrorNodeClient =
        Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
  }

  @Override
  public Optional<AccountInfo> findById(@NonNull AccountId accountId) throws HieroException {
    return mirrorNodeClient.queryAccount(accountId);
  }
}
