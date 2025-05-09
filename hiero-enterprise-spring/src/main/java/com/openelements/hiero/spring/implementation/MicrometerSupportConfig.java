package com.openelements.hiero.spring.implementation;

import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.openelements.hiero.base.interceptors.ReceiveRecordInterceptor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(name = "spring.hiero.metrics.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(name = "org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration")
public class MicrometerSupportConfig {

    public static final String TRANSACTION_TYPE_TAG = "hiero.transaction.record.type";
    public static final String CONTRACT_ID_TAG = "hiero.transaction.record.contractId";
    public static final String TIMER_NAME = "hiero.transaction.record.time";
    public static final String COUNTER_NAME = "hiero.transaction.record";

    @Bean
    @NonNull
    public ReceiveRecordInterceptor interceptRecordReceive(@NonNull final MeterRegistry meterRegistry) {
        return handler -> {
            final String transactionType = handler.transaction().getClass().getSimpleName();
            final Set<Tag> tags = new HashSet<>();
            tags.add(Tag.of(TRANSACTION_TYPE_TAG, transactionType));
            if (handler.transaction() instanceof ContractExecuteTransaction contractExecuteTransaction) {
                tags.add(Tag.of(CONTRACT_ID_TAG,
                        contractExecuteTransaction.getContractId().toString()));
            }
            final Timer timer = meterRegistry.timer(TIMER_NAME, tags);
            final Counter counter = meterRegistry.counter(COUNTER_NAME, tags);
            return timer.record(() -> {
                try {
                    final TransactionRecord transactionRecord = handler.handle();
                    counter.increment();
                    return transactionRecord;
                } catch (Exception e) {
                    throw new RuntimeException("Error in handling record interceptor", e);
                }
            });
        };
    }
}
