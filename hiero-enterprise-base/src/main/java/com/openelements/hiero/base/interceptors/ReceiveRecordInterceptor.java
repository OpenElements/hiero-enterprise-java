package com.openelements.hiero.base.interceptors;

import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface ReceiveRecordInterceptor {

    ReceiveRecordInterceptor DEFAULT_INTERCEPTOR = data -> data.handle();

    @NonNull
    TransactionRecord getRecordFor(@NonNull ReceiveRecordHandler handler) throws Exception;

    record ReceiveRecordHandler(@NonNull Transaction transaction, @NonNull TransactionReceipt receipt,
                                @NonNull ReceiveRecordFunction function) {

        public ReceiveRecordHandler {
            Objects.requireNonNull(transaction, "transaction must not be null");
            Objects.requireNonNull(receipt, "receipt must not be null");
            Objects.requireNonNull(function, "handler must not be null");
        }

        @NonNull
        public TransactionRecord handle() throws Exception {
            return function.handle(receipt);
        }
    }

    @FunctionalInterface
    interface ReceiveRecordFunction {
        @NonNull
        TransactionRecord handle(@NonNull TransactionReceipt receipt) throws Exception;
    }
}
