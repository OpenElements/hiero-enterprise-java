package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;

public interface QueryRequest {
  Hbar queryPayment();

  Hbar maxQueryPayment();
}
