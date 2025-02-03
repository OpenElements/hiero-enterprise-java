package com.openelements.hiero.base.config.provider;

import com.openelements.hiero.base.config.NetworkSettings;
import java.util.Set;

public interface NetworkSettingsProvider {

    String getName();

    Set<NetworkSettings> createNetworkSettings();
}
