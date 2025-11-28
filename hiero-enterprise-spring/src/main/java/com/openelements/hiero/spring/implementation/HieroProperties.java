package com.openelements.hiero.spring.implementation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "spring.hiero")
public class HieroProperties {

  /** Account ID to use for transactions (operator account). */
  private String accountId;

  /** Private key to use for transactions (operator account). */
  private String privateKey;

  @NestedConfigurationProperty
  private HieroNetworkProperties network = new HieroNetworkProperties();

  public String getAccountId() {
    return this.accountId;
  }

  public String getPrivateKey() {
    return this.privateKey;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public HieroNetworkProperties getNetwork() {
    return network;
  }

  public void setNetwork(HieroNetworkProperties network) {
    this.network = network;
  }
}
