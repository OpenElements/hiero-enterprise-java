package com.openelements.hiero.base.interceptors;

import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * First simple interceptor for receiving a record. This interceptor is used to intercept the call
 * for receiving a record for a transaction. Frameworks like Spring can use this interceptor to add
 * functionalities like metrics, tracing, or logging to the calls.
 */
@FunctionalInterface
public interface ReceiveRecordInterceptor {

  /** Default interceptor that does nothing. */
  ReceiveRecordInterceptor DEFAULT_INTERCEPTOR = data -> data.handle();

  /**
   * Intercept the call for receiving a record for a transaction.
   *
   * @param handler the handler that will be used to receive the record
   * @return the record for the transaction
   * @throws Exception if the interceptor fails
   */
  @NonNull TransactionRecord getRecordFor(@NonNull ReceiveRecordHandler handler) throws Exception;

  /**
   * Handler for receiving a record for a transaction.
   *
   * @param transaction the transaction for which the record is received
   * @param receipt the receipt for the transaction
   * @param function the function that will be used to receive the record
   */
  record ReceiveRecordHandler(
      @NonNull Transaction transaction,
      @NonNull TransactionReceipt receipt,
      @NonNull ReceiveRecordFunction function) {

    public ReceiveRecordHandler {
      Objects.requireNonNull(transaction, "transaction must not be null");
      Objects.requireNonNull(receipt, "receipt must not be null");
      Objects.requireNonNull(function, "handler must not be null");
    }

    /**
     * Handle the call for receiving a record for a transaction.
     *
     * @return the record for the transaction
     * @throws Exception if the interceptor fails
     */
    @NonNull
    public TransactionRecord handle() throws Exception {
      return function.handle(receipt);
    }
  }

  /** Function that will be used to receive the record for a transaction. */
  @FunctionalInterface
  interface ReceiveRecordFunction {

    /**
     * Handle the call for receiving a record for a transaction.
     *
     * @param receipt the receipt for the transaction
     * @return the record for the transaction
     * @throws Exception if the interceptor fails
     */
    @NonNull TransactionRecord handle(@NonNull TransactionReceipt receipt) throws Exception;
  }
}
