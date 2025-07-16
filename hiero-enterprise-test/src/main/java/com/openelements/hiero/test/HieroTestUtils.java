package com.openelements.hiero.test;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.mirrornode.TopicRepository;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.TransactionListener;
import com.openelements.hiero.base.protocol.data.TransactionType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HieroTestUtils implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(HieroTestUtils.class);

    private final MirrorNodeClient mirrorNodeClient;

    private final ProtocolLayerClient protocolLayerClient;

    private final AtomicReference<TransactionId> transactionIdRef = new AtomicReference<>();

    public HieroTestUtils(MirrorNodeClient mirrorNodeClient, ProtocolLayerClient protocolLayerClient) {
        this.mirrorNodeClient = Objects.requireNonNull(mirrorNodeClient, "MirrorNodeClient cannot be null");
        this.protocolLayerClient = Objects.requireNonNull(protocolLayerClient, "ProtocolLayerClient cannot be null");

        protocolLayerClient.addTransactionListener(new TransactionListener() {
            @Override
            public void transactionSubmitted(TransactionType transactionType, TransactionId transactionId) {
                log.debug("Transaction submitted: type={}, id={}", transactionType, transactionId);
                transactionIdRef.set(transactionId);
            }

            @Override
            public void transactionHandled(TransactionType transactionType, TransactionId transactionId,
                                           Status transactionStatus) {
                log.debug("Transaction handled: type={}, id={}, status={}", transactionType, transactionId, transactionStatus);
            }
        });
    }

    /**
     * Waits for a topic to be available on the mirror node.
     *
     * @param topicId         The TopicId to check for on the mirror node.
     * @param topicRepository The TopicRepository to query the mirror node.
     * @throws HieroException If the topic is not found within the timeout period or an error occurs.
     */
    public void waitForMirrorNodeRecords(TopicId topicId, TopicRepository topicRepository) throws HieroException {
        Objects.requireNonNull(topicId, "TopicId cannot be null");
        Objects.requireNonNull(topicRepository, "TopicRepository cannot be null");

        log.debug("Waiting for topic '{}' to be available on mirror node", topicId);
        final LocalDateTime start = LocalDateTime.now();
        final long timeoutSeconds = 30;

        while (true) {
            try {
                Optional<?> topic = topicRepository.findTopicById(topicId);
                if (topic.isPresent()) {
                    log.debug("Topic '{}' is available on mirror node", topicId);
                    return;
                }
            } catch (HieroException e) {
                log.error("Error querying mirror node for topic {}: {}", topicId, e.getMessage());
                throw new HieroException("Failed to query mirror node for topic " + topicId, e);
            }

            if (LocalDateTime.now().isAfter(start.plusSeconds(timeoutSeconds))) {
                throw new HieroException("Timeout waiting for topic " + topicId + " to appear on mirror node after " + timeoutSeconds + " seconds");
            }

            try {
                Thread.sleep(1000); // Wait 1 second before retrying
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new HieroException("Interrupted while waiting for topic " + topicId, e);
            }
        }
    }

    /**
     * Waits for a transaction to be available on the mirror node (legacy method).
     *
     * @throws HieroException If the transaction is not found within the timeout period or an error occurs.
     */
    public void waitForMirrorNodeRecords() throws HieroException {
        final TransactionId transactionId = transactionIdRef.get();
        if (transactionId == null) {
            log.debug("No transaction to wait for");
            return;
        }

        log.debug("Waiting for transaction '{}' to be available on mirror node", transactionId);
        final LocalDateTime start = LocalDateTime.now();
        final long timeoutSeconds = 30;

        String transactionIdString = transactionId.accountId.toString() + "@" + transactionId.validStart.getEpochSecond() + "."
                + String.format("%09d", transactionId.validStart.getNano());

        while (true) {
            try {
                if (mirrorNodeClient.queryTransaction(transactionIdString).isPresent()) {
                    log.debug("Transaction '{}' is available on mirror node", transactionId);
                    return;
                }
            } catch (HieroException e) {
                log.error("Error querying mirror node for transaction {}: {}", transactionIdString, e.getMessage());
                throw new HieroException("Failed to query mirror node for transaction " + transactionIdString, e);
            }

            if (LocalDateTime.now().isAfter(start.plusSeconds(timeoutSeconds))) {
                throw new HieroException("Timeout waiting for transaction " + transactionIdString + " after " + timeoutSeconds + " seconds");
            }

            try {
                Thread.sleep(100); // Wait 100ms before retrying
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new HieroException("Interrupted while waiting for transaction " + transactionIdString, e);
            }
        }
    }
}
