package com.openelements.hiero.base.config.implementation;

import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.config.NetworkSettings;
import com.openelements.hiero.base.data.Account;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public class NetworkSettingsBasedHieroConfig implements HieroConfig {

    private final Account operatorAccount;

    private final NetworkSettings networkSetting;

    public NetworkSettingsBasedHieroConfig(@NonNull final Account operatorAccount, @NonNull final String networkName) {
        this.operatorAccount = Objects.requireNonNull(operatorAccount, "operatorAccount must not be null");
        Objects.requireNonNull(networkName, "networkName must not be null");
        this.networkSetting = NetworkSettings.forIdentifier(networkName)
                .orElseThrow(() -> new IllegalStateException("Network settings for '" + networkName + "' not found"));
    }

    @Override
    public @NonNull Account getOperatorAccount() {
        return operatorAccount;
    }

    @Override
    public @NonNull Optional<String> getNetworkName() {
        return networkSetting.getNetworkName();
    }

    @Override
    public @NonNull Set<String> getMirrorNodeAddresses() {
        return networkSetting.getMirrorNodeAddresses();
    }

    @Override
    public @NonNull Set<String> getConsensusServiceAddress() {
        return networkSetting.getConsensusServiceAddress();
    }

    @Override
    public @NonNull Set<ConsensusNode> getConsensusNodes() {
        return networkSetting.getConsensusNodes();
    }

    @Override
    public @NonNull Optional<Long> chainId() {
        return networkSetting.chainId();
    }

    @Override
    public @NonNull Optional<String> relayUrl() {
        return networkSetting.relayUrl();
    }
}
