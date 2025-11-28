package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;

public interface TransactionRecord extends TransactionResult {

  TransactionId transactionId();

  Status status();

  byte[] transactionHash();

  Instant consensusTimestamp();

  Hbar transactionFee();
}
