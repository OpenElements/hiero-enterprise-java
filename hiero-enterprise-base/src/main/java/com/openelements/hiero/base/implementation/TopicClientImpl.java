package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.SubscriptionHandle;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TopicMessage;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.TopicClient;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.TopicCreateRequest;
import com.openelements.hiero.base.protocol.data.TopicCreateResult;
import com.openelements.hiero.base.protocol.data.TopicUpdateRequest;
import com.openelements.hiero.base.protocol.data.TopicDeleteRequest;
import com.openelements.hiero.base.protocol.data.TopicSubmitMessageRequest;
import com.openelements.hiero.base.protocol.data.TopicMessageRequest;
import com.openelements.hiero.base.protocol.data.TopicMessageResult;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

public class TopicClientImpl implements TopicClient {
    private final ProtocolLayerClient client;

    private final Account operationalAccount;

    public TopicClientImpl(@NonNull final ProtocolLayerClient client, @NonNull final Account operationalAccount) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.operationalAccount = Objects.requireNonNull(operationalAccount, "operationalAccount must not be null");
    }

    @Override
    public @NonNull TopicId createTopic() throws HieroException {
        return createTopic(operationalAccount.privateKey());
    }

    @Override
    public @NonNull TopicId createTopic(@NonNull PrivateKey adminKey) throws HieroException {
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        TopicCreateRequest request = TopicCreateRequest.of(adminKey);
        TopicCreateResult result = client.executeTopicCreateTransaction(request);
        return result.topicId();
    }

    @Override
    public @NonNull TopicId createTopic(@NonNull String memo) throws HieroException {
        Objects.requireNonNull(memo, "memo must not be null");
        return createTopic(operationalAccount.privateKey(), memo);
    }

    @Override
    public @NonNull TopicId createTopic(@NonNull PrivateKey adminKey, @NonNull String memo) throws HieroException {
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        Objects.requireNonNull(memo, "memo must not be null");
        TopicCreateRequest request = TopicCreateRequest.of(adminKey, memo);
        TopicCreateResult result = client.executeTopicCreateTransaction(request);
        return result.topicId();
    }

    @Override
    public @NonNull TopicId createPrivateTopic(@NonNull PrivateKey submitKey) throws HieroException {
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        return createPrivateTopic(operationalAccount.privateKey(), submitKey);
    }

    @Override
    public @NonNull TopicId createPrivateTopic(@NonNull PrivateKey adminKey, @NonNull PrivateKey submitKey)
            throws HieroException {
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        TopicCreateRequest request = TopicCreateRequest.of(adminKey, submitKey);
        TopicCreateResult result = client.executeTopicCreateTransaction(request);
        return result.topicId();
    }

    @Override
    public @NonNull TopicId createPrivateTopic(@NonNull PrivateKey submitKey, @NonNull String memo)
            throws HieroException {
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        Objects.requireNonNull(memo, "memo must not be null");
        return createPrivateTopic(operationalAccount.privateKey(), submitKey, memo);
    }

    @Override
    public @NonNull TopicId createPrivateTopic(@NonNull PrivateKey adminKey, @NonNull PrivateKey submitKey,
                                               @NonNull String memo) throws HieroException {
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        Objects.requireNonNull(memo, "memo must not be null");
        TopicCreateRequest request = TopicCreateRequest.of(adminKey, submitKey, memo);
        TopicCreateResult result = client.executeTopicCreateTransaction(request);
        return result.topicId();
    }

    @Override
    public void updateTopic(@NonNull TopicId topicId, @NonNull String memo) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(memo, "memo must not be null");
        updateTopic(topicId, operationalAccount.privateKey(), memo);
    }

    @Override
    public void updateTopic(@NonNull TopicId topicId, @NonNull PrivateKey adminKey, @NonNull String memo)
            throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        Objects.requireNonNull(memo, "memo must not be null");
        TopicUpdateRequest request = TopicUpdateRequest.of(topicId, adminKey, memo);
        client.executeTopicUpdateTransaction(request);
    }

    @Override
    public void updateTopic(@NonNull TopicId topicId, @NonNull PrivateKey updatedAdminKey,
                            @NonNull PrivateKey submitKey, @NonNull String memo) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        Objects.requireNonNull(memo, "memo must not be null");
        updateTopic(topicId, operationalAccount.privateKey(), updatedAdminKey, submitKey, memo);
    }

    @Override
    public void updateTopic(@NonNull TopicId topicId, @NonNull PrivateKey adminKey, @NonNull PrivateKey updatedAdminKey,
                            @NonNull PrivateKey submitKey, @NonNull String memo) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        Objects.requireNonNull(memo, "memo must not be null");
        TopicUpdateRequest request = TopicUpdateRequest.of(topicId, adminKey, updatedAdminKey, submitKey, memo);
        client.executeTopicUpdateTransaction(request);
    }

    @Override
    public void updateAdminKey(@NonNull TopicId topicId, @NonNull PrivateKey updatedAdminKey) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(updatedAdminKey, "updatedAdminKey must not be null");
        updateAdminKey(topicId, operationalAccount.privateKey(), updatedAdminKey);
    }

    @Override
    public void updateAdminKey(@NonNull TopicId topicId, @NonNull PrivateKey adminKey,
                               @NonNull PrivateKey updatedAdminKey) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        Objects.requireNonNull(updatedAdminKey, "updatedAdminKey must not be null");
        TopicUpdateRequest request = TopicUpdateRequest.updateAdminKey(topicId, adminKey, updatedAdminKey);
        client.executeTopicUpdateTransaction(request);
    }

    @Override
    public void updateSubmitKey(@NonNull TopicId topicId, @NonNull PrivateKey submitKey) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        updateSubmitKey(topicId, operationalAccount.privateKey(), submitKey);
    }

    @Override
    public void updateSubmitKey(@NonNull TopicId topicId, @NonNull PrivateKey adminKey, @NonNull PrivateKey submitKey)
            throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        TopicUpdateRequest request = TopicUpdateRequest.updateSubmitKey(topicId, adminKey, submitKey);
        client.executeTopicUpdateTransaction(request);
    }

    @Override
    public void deleteTopic(@NonNull TopicId topicId) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        TopicDeleteRequest request = TopicDeleteRequest.of(operationalAccount.privateKey(), topicId);
        client.executeTopicDeleteTransaction(request);
    }

    @Override
    public void deleteTopic(@NonNull TopicId topicId, @NonNull PrivateKey adminKey) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(adminKey, "adminKey must not be null");
        TopicDeleteRequest request = TopicDeleteRequest.of(adminKey, topicId);
        client.executeTopicDeleteTransaction(request);
    }

    @Override
    public void submitMessage(@NonNull TopicId topicId, @NonNull String message) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(message, "message must not be null");
        submitMessage(topicId, message.getBytes());
    }

    @Override
    public void submitMessage(@NonNull TopicId topicId, @NonNull byte[] message) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(message, "message must not be null");
        TopicSubmitMessageRequest request = TopicSubmitMessageRequest.of(topicId, message);
        client.executeTopicMessageSubmitTransaction(request);
    }

    @Override
    public void submitMessage(@NonNull TopicId topicId, @NonNull PrivateKey submitKey, @NonNull String message) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        Objects.requireNonNull(message, "message must not be null");
        submitMessage(topicId, submitKey, message.getBytes());
    }

    @Override
    public void submitMessage(@NonNull TopicId topicId, @NonNull PrivateKey submitKey, @NonNull byte[] message)
            throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(submitKey, "submitKey must not be null");
        Objects.requireNonNull(message, "message must not be null");
        TopicSubmitMessageRequest request = TopicSubmitMessageRequest.of(topicId, submitKey, message);
        client.executeTopicMessageSubmitTransaction(request);
    }

    @Override
    public SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription)
            throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(subscription, "subscription must not be null");
        TopicMessageRequest request = TopicMessageRequest.of(topicId, subscription);
        TopicMessageResult result = client.executeTopicMessageQuery(request);
        return result.subscriptionHandle();
    }

    @Override
    public SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                             long limit) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(subscription, "subscription must not be null");
        if (limit == 0) {
            throw new IllegalArgumentException("limit must be greater than 0");
        }

        TopicMessageRequest request = TopicMessageRequest.of(topicId, subscription, limit);
        TopicMessageResult result = client.executeTopicMessageQuery(request);
        return result.subscriptionHandle();
    }

    @Override
    public SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                             Instant startTime, Instant endTime) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(subscription, "subscription must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");
        Objects.requireNonNull(endTime, "endTime must not be null");

        if (startTime.isBefore(Instant.now())) {
            throw  new IllegalArgumentException("startTime must be greater than currentTime");
        }
        if (endTime.isBefore(startTime)) {
            throw  new IllegalArgumentException("endTime must be greater than startTime");
        }

        TopicMessageRequest request = TopicMessageRequest.of(topicId, subscription, startTime, endTime);
        TopicMessageResult result = client.executeTopicMessageQuery(request);
        return result.subscriptionHandle();
    }

    @Override
    public SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                             @NonNull Instant startTime, @NonNull Instant endTime, long limit)
            throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(subscription, "subscription must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");
        Objects.requireNonNull(endTime, "endTime must not be null");

        if (startTime.isBefore(Instant.now())) {
            throw  new IllegalArgumentException("startTime must be greater than currentTime");
        }
        if (endTime.isBefore(startTime)) {
            throw  new IllegalArgumentException("endTime must be greater than startTime");
        }
        if (limit == 0) {
            throw new IllegalArgumentException("limit must be greater than 0");
        }

        TopicMessageRequest request = TopicMessageRequest.of(topicId, subscription, startTime, endTime, limit);
        TopicMessageResult result = client.executeTopicMessageQuery(request);
        return result.subscriptionHandle();
    }
}
