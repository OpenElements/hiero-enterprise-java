package com.openelements.hiero.base.config.hedera;

import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.NetworkSettings;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public final class HederMainnetSettings implements NetworkSettings {

    public static final String NETWORK_IDENTIFIER = "hedera-mainnet";

    @Override
    public @NonNull String getNetworkIdentifier() {
        return NETWORK_IDENTIFIER;
    }

    @Override
    public @NonNull Optional<String> getNetworkName() {
        return Optional.of("Hedera Mainnet");
    }

    @Override
    public @NonNull Set<String> getMirrorNodeAddresses() {
        return Set.of("https://mainnet.mirrornode.hedera.com:443");
    }

    @Override
    public @NonNull Set<ConsensusNode> getConsensusNodes() {
        return Set.of(new ConsensusNode("35.186.191.247", "50211", "0.0.4"));
    }

    @Override
    public @NonNull Optional<Long> chainId() {
        return Optional.of(295L);
    }

    @Override
    public @NonNull Optional<String> relayUrl() {
        return Optional.of("https://mainnet.hashio.io/api");
    }
}
