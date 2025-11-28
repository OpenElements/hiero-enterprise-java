package com.openelements.hiero.base.config;

import com.hedera.hashgraph.sdk.AccountId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Configuration for a consensus node in a Hiero network.
 *
 * @param ip the IP address of the consensus node
 * @param port the port of the consensus node
 * @param account the account ID of the consensus node
 */
public record ConsensusNode(@NonNull String ip, @NonNull String port, @NonNull String account) {

  public ConsensusNode {
    Objects.requireNonNull(ip, "ip must not be null");
    Objects.requireNonNull(port, "port must not be null");
    Objects.requireNonNull(account, "account must not be null");
  }

  /**
   * Get the address of the consensus node. The address is the IP address and port of the consensus
   * node.
   *
   * @return the address
   */
  public String getAddress() {
    return ip + ":" + port;
  }

  /**
   * Get the account ID of the consensus node.
   *
   * @return the account ID
   */
  public AccountId getAccountId() {
    return AccountId.fromString(account);
  }
}
