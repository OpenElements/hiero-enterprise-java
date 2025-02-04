package com.openelements.hiero.base.config;

import java.util.Set;
import org.jspecify.annotations.NonNull;

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
    @NonNull
    String getName();

    /**
     * Return a set of {@link NetworkSettings} instances provided by this provider.
     *
     * @return a set of {@link NetworkSettings} instances
     */
    @NonNull
    Set<NetworkSettings> createNetworkSettings();
}
