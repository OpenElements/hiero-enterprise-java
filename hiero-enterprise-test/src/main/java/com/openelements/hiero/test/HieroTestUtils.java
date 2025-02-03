package com.openelements.hiero.test;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.TransactionListener;
import com.openelements.hiero.base.protocol.TransactionType;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HieroTestUtils {

    private final MirrorNodeClient mirrorNodeClient;

    private final ProtocolLayerClient protocolLayerClient;

    private final AtomicReference<TransactionId> transactionIdRef = new AtomicReference<>();

    private final AtomicBoolean initialized = new AtomicBoolean();

    public HieroTestUtils(MirrorNodeClient mirrorNodeClient, ProtocolLayerClient protocolLayerClient) {
        this.mirrorNodeClient = mirrorNodeClient;
        this.protocolLayerClient = protocolLayerClient;
    }

    public synchronized void init() {
        final boolean initHappened = initialized.get();
        if (initHappened) {
            return;
        }
        initialized.set(true);
        protocolLayerClient.addTransactionListener(new TransactionListener() {
            @Override
            public void transactionSubmitted(TransactionType transactionType, TransactionId transactionId) {
                transactionIdRef.set(transactionId);
            }

            @Override
            public void transactionHandled(TransactionType transactionType, TransactionId transactionId,
                    Status transactionStatus) {
            }
        });
    }

    public void waitForMirrorNodeRecords() throws HieroException {
        final TransactionId transactionId = transactionIdRef.get();
        if (transactionId != null) {
            final LocalDateTime start = LocalDateTime.now();
            boolean done = false;
            while (!done) {
                String transactionIdString =
                        transactionId.accountId.toString() + "-" + transactionId.validStart.getEpochSecond() + "-"
                                + String.format("%09d", transactionId.validStart.getNano());
                done = mirrorNodeClient.queryTransaction(transactionIdString).isPresent();
                if (!done) {
                    if (LocalDateTime.now().isAfter(start.plusSeconds(30))) {
                        throw new HieroException("Timeout waiting for transaction");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new HieroException("Interrupted while waiting for transaction", e);
                    }
                }
            }
        }
    }
}
