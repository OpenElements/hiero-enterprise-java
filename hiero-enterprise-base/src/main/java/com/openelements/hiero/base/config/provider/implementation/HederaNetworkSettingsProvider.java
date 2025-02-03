package com.openelements.hiero.base.config.provider.implementation;

import com.openelements.hiero.base.config.NetworkSettings;
import com.openelements.hiero.base.config.provider.HederMainnetSettings;
import com.openelements.hiero.base.config.provider.NetworkSettingsProvider;
import java.util.Set;

public final class HederaNetworkSettingsProvider implements NetworkSettingsProvider {

    @Override
    public String getName() {
        return "Hedera";
    }

    @Override
    public Set<NetworkSettings> createNetworkSettings() {
        return Set.of(new HederMainnetSettings());
    }
}
