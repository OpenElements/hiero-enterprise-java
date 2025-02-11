package com.openelements.hiero.base.test.config;

import com.openelements.hiero.base.config.NetworkSettings;
import com.openelements.hiero.base.config.NetworkSettingsProvider;
import java.util.Set;

public class SoloActionNetworkSettingsProvider implements NetworkSettingsProvider {

    @Override
    public String getName() {
        return "Provider for Hiero Solo Action";
    }

    @Override
    public Set<NetworkSettings> createNetworkSettings() {
        return Set.of(new SoloActionNetworkSettings());
    }
}
