package com.openelements.hiero.test;

import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.NetworkSettings;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;


public class SoloActionNetworkSettings implements NetworkSettings {

    @Override
    public @NonNull String getNetworkIdentifier() {
        return "hiero-solo-action";
    }

    @Override
    public @NonNull Optional<String> getNetworkName() {
        return Optional.of("Hiero Solo Action");
    }

    @Override
    public @NonNull Set<String> getMirrorNodeAddresses() {
        return Set.of("http://localhost:8080");
    }

    @Override
    public @NonNull Set<String> getConsensusServiceAddress() {return Set.of("http://localhost:8080");}

    @Override
    public @NonNull Set<ConsensusNode> getConsensusNodes() {
        return Set.of(new ConsensusNode("127.0.0.1", "50211", "0.0.3"));
    }

    @Override
    public @NonNull Optional<Long> chainId() {
        return Optional.empty();
    }

    @Override
    public @NonNull Optional<String> relayUrl() {
        return Optional.empty();
    }
}
