package com.openelements.hiero.base.implementation;

import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.mirrornode.NetworkRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class NetworkRepositoryImpl implements NetworkRepository {
  private final MirrorNodeClient mirrorNodeClient;

  public NetworkRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
    this.mirrorNodeClient =
        Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
  }

  @Override
  public Optional<ExchangeRates> exchangeRates() throws HieroException {
    return mirrorNodeClient.queryExchangeRates();
  }

  @Override
  public List<NetworkFee> fees() throws HieroException {
    return mirrorNodeClient.queryNetworkFees();
  }

  @Override
  public Optional<NetworkStake> stake() throws HieroException {
    return mirrorNodeClient.queryNetworkStake();
  }

  @Override
  public Optional<NetworkSupplies> supplies() throws HieroException {
    return mirrorNodeClient.queryNetworkSupplies();
  }
}
