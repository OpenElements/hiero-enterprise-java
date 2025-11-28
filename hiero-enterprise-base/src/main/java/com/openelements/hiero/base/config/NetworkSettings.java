package com.openelements.hiero.base.config;

import com.openelements.hiero.base.config.implementation.NetworkSettingsProviderLoader;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;

/**
 * Interface that provides all needed configuration settings for a network. Operator of a Hiero
 * based network should implement this interface to provide the necessary configuration settings.
 * Implementations can be provided via Java SPI as defined in {@link NetworkSettingsProvider}.
 *
 * @see NetworkSettingsProvider
 * @see java.util.ServiceLoader
 */
public interface NetworkSettings {

  /**
   * Returns the network identifier.
   *
   * @return the network identifier
   */
  @NonNull String getNetworkIdentifier();

  /**
   * Returns the network name.
   *
   * @return the network name
   */
  @NonNull Optional<String> getNetworkName();

  /**
   * Returns the mirror node addresses.
   *
   * @return the mirror node addresses
   */
  @NonNull Set<String> getMirrorNodeAddresses();

  /**
   * Returns the consensus nodes.
   *
   * @return the consensus nodes
   */
  @NonNull Set<ConsensusNode> getConsensusNodes();

  /**
   * Returns the chain ID of the network.
   *
   * @return the chain ID of the network
   */
  @NonNull Optional<Long> chainId();

  /**
   * Returns the relay URL of the network.
   *
   * @return the relay URL of the network.
   */
  @NonNull Optional<String> relayUrl();

  /**
   * Returns all available network settings.
   *
   * @return all available network settings
   */
  @NonNull
  static Set<NetworkSettings> all() {
    return NetworkSettingsProviderLoader.getInstance().all();
  }

  /**
   * Returns the network settings for the given identifier.
   *
   * @param identifier the identifier of the network
   * @return the network settings for the given identifier
   */
  @NonNull
  static Optional<NetworkSettings> forIdentifier(@NonNull String identifier) {
    Objects.requireNonNull(identifier, "identifier must not be null");
    return NetworkSettingsProviderLoader.getInstance().forIdentifier(identifier);
  }
}
