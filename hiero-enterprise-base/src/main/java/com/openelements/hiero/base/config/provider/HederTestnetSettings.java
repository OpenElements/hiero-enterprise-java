package com.openelements.hiero.base.config.provider;

import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.NetworkSettings;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public final class HederTestnetSettings implements NetworkSettings {

    public static final String NETWORK_IDENTIFIER = "hedera-testnet";

    @Override
    public @NonNull String getNetworkIdentifier() {
        return NETWORK_IDENTIFIER;
    }

    @Override
    public @NonNull Optional<String> getNetworkName() {
        return Optional.of("Hedera Testnet");
    }

    @Override
    public @NonNull Set<String> getMirrorNodeAddresses() {
        return Set.of("https://testnet.mirrornode.hedera.com/");
    }

    @Override
    public @NonNull Set<ConsensusNode> getConsensusNodes() {
        return Set.of();
    }

    @Override
    public @NonNull Optional<Long> chainId() {
        return Optional.of(296L);
    }

    @Override
    public @NonNull Optional<String> relayUrl() {
        return Optional.of("https://testnet.hashio.io/api");
    }
}
