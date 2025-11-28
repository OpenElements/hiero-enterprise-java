package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.Client;
import com.openelements.hiero.base.data.Account;
import org.jspecify.annotations.NonNull;

/** Context for a specific Hiero connection to a network. */
public interface HieroContext {

  /**
   * Get the account that is used to pay for the transactions that are sent to the network. This
   * account is called the 'operator account'.
   *
   * @return the operator account
   */
  @NonNull Account getOperatorAccount();

  /**
   * Get the 'native' client that is used to interact with the hiero network.
   *
   * @return the client
   */
  @NonNull Client getClient();
}
