package com.openelements.hiero.test.implementation;

import com.google.auto.service.AutoService;
import com.openelements.hiero.base.config.NetworkSettings;
import com.openelements.hiero.base.config.NetworkSettingsProvider;
import com.openelements.hiero.test.SoloActionNetworkSettings;
import java.util.Set;

@AutoService(NetworkSettingsProvider.class)
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
