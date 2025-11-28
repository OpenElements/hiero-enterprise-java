package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;
import org.jspecify.annotations.NonNull;

public interface TransactionRequest {

  Hbar DEFAULT_MAX_TRANSACTION_FEE = new Hbar(10);

  Duration DEFAULT_TRANSACTION_VALID_DURATION = Duration.ofSeconds(120);

  @NonNull Hbar maxTransactionFee();

  @NonNull Duration transactionValidDuration();
}
