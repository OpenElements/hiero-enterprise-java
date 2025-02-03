package com.openelements.hiero.base.config;

import com.openelements.hiero.base.config.provider.implementation.NetworkSettingsProviderLoader;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NetworkSettings {

    Logger logger = LoggerFactory.getLogger(NetworkSettings.class);

    /**
     * Returns the network identifier.
     *
     * @return the network identifier
     */
    @NonNull
    String getNetworkIdentifier();

    /**
     * Returns the network name.
     *
     * @return the network name
     */
    @NonNull
    Optional<String> getNetworkName();

    /**
     * Returns the mirror node addresses.
     *
     * @return the mirror node addresses
     */
    @NonNull
    Set<String> getMirrorNodeAddresses();

    /**
     * Returns the consensus nodes.
     *
     * @return the consensus nodes
     */
    @NonNull
    Set<ConsensusNode> getConsensusNodes();

    @NonNull
    Optional<Long> chainId();

    @NonNull
    Optional<String> relayUrl();

    static Set<NetworkSettings> all() {
        return NetworkSettingsProviderLoader.getInstance().all();
    }

    static Optional<NetworkSettings> forIdentifier(String identifier) {
        return NetworkSettingsProviderLoader.getInstance().forIdentifier(identifier);
    }
}
