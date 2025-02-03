package com.openelements.hiero.base.config.provider;

import com.openelements.hiero.base.config.NetworkSettings;
import java.util.Set;

/**
 * SPI interface to provide predefined {@link NetworkSettings} instances. Java SPI functionality is documented at
 * {@link java.util.ServiceLoader}.
 */
public interface NetworkSettingsProvider {

    /**
     * Returns the name of the provider.
     *
     * @return the name of the provider
     */
    String getName();

    /**
     * Return a set of {@link NetworkSettings} instances provided by this provider.
     *
     * @return a set of {@link NetworkSettings} instances
     */
    Set<NetworkSettings> createNetworkSettings();
}
