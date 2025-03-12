package com.openelements.hiero.test;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.TransactionListener;
import com.openelements.hiero.base.protocol.data.TransactionType;

public class HieroTestUtils implements Serializable {

    private final static Logger log = LoggerFactory.getLogger(HieroTestUtils.class);

    private final MirrorNodeClient mirrorNodeClient;

    private final ProtocolLayerClient protocolLayerClient;

    private final AtomicReference<TransactionId> transactionIdRef = new AtomicReference<>();

    public HieroTestUtils() {
        throw new UnsupportedOperationException("No-args constructor not supported");
    }

    public HieroTestUtils(MirrorNodeClient mirrorNodeClient, ProtocolLayerClient protocolLayerClient) {
        this.mirrorNodeClient = mirrorNodeClient;
        this.protocolLayerClient = protocolLayerClient;

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

    public void waitForMirrorNodeRecords() {
        final TransactionId transactionId = transactionIdRef.get();
        if (transactionId != null) {
            log.debug("Waiting for transaction '{}' available at mirror node", transactionId);
            final LocalDateTime start = LocalDateTime.now();
            boolean done = false;
            while (!done) {
                String transactionIdString =
                        transactionId.accountId.toString() + "-" + transactionId.validStart.getEpochSecond() + "-"
                                + String.format("%09d", transactionId.validStart.getNano());
                try {
                    done = mirrorNodeClient.queryTransaction(transactionIdString).isPresent();
                } catch (HieroException e) {
                    throw new RuntimeException("Error in mirror node query!", e);
                }
                if (!done) {
                    if (LocalDateTime.now().isAfter(start.plusSeconds(60))) {
                        throw new RuntimeException("Timeout waiting for transaction");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Interrupted while waiting for transaction", e);
                    }
                }
            }
            log.debug("Transaction '{}' is available at mirror node", transactionId);
        } else {
            log.debug("No transaction to wait for");
        }
    }
}
