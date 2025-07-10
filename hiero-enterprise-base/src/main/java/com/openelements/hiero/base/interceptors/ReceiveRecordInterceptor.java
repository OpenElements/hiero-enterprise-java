package com.openelements.hiero.base.interceptors;

import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;

/**
 * First simple interceptor for receiving a record. This interceptor is used to intercept the call for receiving a
 * record for a transaction. Frameworks like Spring can use this interceptor to add functionalities like metrics,
 * tracing, or logging to the calls.
 */
@FunctionalInterface
public interface ReceiveRecordInterceptor {

    /**
     * Default interceptor that does nothing.
     */
    ReceiveRecordInterceptor DEFAULT_INTERCEPTOR = invocationContext -> invocationContext.proceed();

    /**
     * Intercept the call for receiving a record for a transaction.
     *
     * @param invocationContext the context that will be used to receive the record
     * @return the record for the transaction
     * @throws Exception if the interceptor fails
     */
    @NonNull
    TransactionRecord getRecordFor(@NonNull InvocationContext invocationContext)
            throws Exception;

    /**
     * Handler for receiving a record for a transaction.
     *
     * @param transaction     the transaction for which the record is received
     * @param receipt         the receipt for the transaction
     * @param innerInvocation the function that will be used to receive the record
     */
    record InvocationContext(@NonNull Transaction transaction, @NonNull TransactionReceipt receipt,
                             @NonNull Function<TransactionReceipt, TransactionRecord> innerInvocation) {

        public InvocationContext {
            Objects.requireNonNull(transaction, "transaction must not be null");
            Objects.requireNonNull(receipt, "receipt must not be null");
            Objects.requireNonNull(innerInvocation, "innerInvocation must not be null");
        }

        /**
         * Handle the call for receiving a record for a transaction.
         *
         * @return the record for the transaction
         */
        @NonNull
        public TransactionRecord proceed() {
            return innerInvocation.apply(receipt);
        }
    }
}
