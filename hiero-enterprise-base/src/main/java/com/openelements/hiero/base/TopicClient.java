package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.SubscriptionHandle;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TopicMessage;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Interface for interacting with a Hiero network. This interface provides methods for interacting with Hiero Topic,
 * like creating, updating and deleting topic. An implementation of this interface is using an internal account to
 * interact with a Hiero network. That account is the account that is used to pay for the transactions that are sent to
 * the Hiero network and called 'operator account'.
 */
public interface TopicClient {
    /**
     * Create a new Topic. The operator account privateKey is used as adminKey for topic.
     *
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createTopic() throws HieroException;

    /**
     * Create a new Topic. With an adminKey.
     *
     * @param adminKey the adminKey for the topic
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createTopic(@NonNull PrivateKey adminKey) throws HieroException;

    /**
     * Create a new Topic.
     *
     * @param memo the memo for the topic
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createTopic(@NonNull String memo) throws HieroException;

    /**
     * Create a new Topic.
     *
     * @param adminKey the adminKey for the topic
     * @param memo the memo for the topic
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createTopic(@NonNull PrivateKey adminKey, @NonNull String memo) throws HieroException;

    /**
     * Create a new private Topic.
     *
     * @param submitKey the submitKey for the topic
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createPrivateTopic(@NonNull PrivateKey submitKey) throws HieroException;

    /**
     * Create a new private Topic.
     *
     * @param adminKey the adminKey for the topic
     * @param submitKey the submitKey for the topic
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createPrivateTopic(@NonNull PrivateKey adminKey, @NonNull PrivateKey submitKey) throws HieroException;

    /**
     * Create a new private Topic.
     *
     * @param submitKey the submitKey for the topic
     * @param memo the memo for the topic
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createPrivateTopic(@NonNull PrivateKey submitKey, @NonNull String memo) throws HieroException;

    /**
     * Create a new private Topic.
     *
     * @param adminKey the adminKey for the topic
     * @param submitKey the submitKey for the topic
     * @param memo the memo for the topic
     * @return the ID of the new Topic
     * @throws HieroException if the Topic could not be created
     */
    @NonNull
    TopicId createPrivateTopic(@NonNull PrivateKey adminKey, @NonNull PrivateKey submitKey, @NonNull String memo)
            throws HieroException;

    /**
     * Update a Topic. The operator account privateKey is used as adminKey for updating topic.
     *
     * @param topicId the topicId of topic to update
     * @param memo the memo for the topic
     * @throws HieroException if the Topic could not be created
     */
    void updateTopic(@NonNull TopicId topicId, @NonNull String memo) throws HieroException;

    /**
     * Update a Topic.
     *
     * @param topicId the topicId of topic to update
     * @param adminKey the adminKey for topic
     * @param memo the memo for the topic
     * @throws HieroException if the Topic could not be created
     */
    void updateTopic(@NonNull TopicId topicId, @NonNull PrivateKey adminKey, @NonNull String memo)
            throws HieroException;

    /**
     * Update a Topic. The operator account privateKey is used as adminKey for updating topic.
     *
     * @param topicId the topicId of topic to update
     * @param updatedAdminKey the new adminKey for topic
     * @param submitKey the new submit for topic
     * @param memo the memo for the topic
     * @throws HieroException if the Topic could not be created
     */
    void updateTopic(@NonNull TopicId topicId, @NonNull PrivateKey updatedAdminKey, @NonNull PrivateKey submitKey,
                     @NonNull String memo) throws HieroException;

    /**
     * Update a Topic.
     *
     * @param topicId the topicId of topic to update
     * @param adminKey the adminKey of topic
     * @param updatedAdminKey the new adminKey for topic
     * @param submitKey the new submit for topic
     * @param memo the memo for the topic
     * @throws HieroException if the Topic could not be created
     */
    void updateTopic(@NonNull TopicId topicId, @NonNull PrivateKey adminKey, @NonNull PrivateKey updatedAdminKey,
                     @NonNull PrivateKey submitKey, @NonNull String memo) throws HieroException;

    /**
     * Update adminKey for Topic. The operator account privateKey is used as adminKey for updating topic.
     *
     * @param topicId the topicId of topic to update
     * @param updatedAdminKey the new adminKey for topic
     * @throws HieroException if the Topic could not be created
     */
    void updateAdminKey(@NonNull TopicId topicId, @NonNull PrivateKey updatedAdminKey) throws HieroException;

    /**
     * Update adminKey for Topic.
     *
     * @param topicId the topicId of topic to update
     * @param adminKey the adminKey of topic
     * @param updatedAdminKey the new adminKey for topic
     * @throws HieroException if the Topic could not be created
     */
    void updateAdminKey(@NonNull TopicId topicId, @NonNull PrivateKey adminKey, @NonNull PrivateKey updatedAdminKey)
            throws HieroException;

    /**
     * Update submitKey for Topic. The operator account privateKey is used as adminKey for updating topic.
     *
     * @param topicId the topicId of topic to update
     * @param submitKey the new submitKey for topic
     * @throws HieroException if the Topic could not be created
     */
    void updateSubmitKey(@NonNull TopicId topicId, @NonNull PrivateKey submitKey) throws HieroException;

    /**
     * Update submitKey for Topic.
     *
     * @param topicId the topicId of topic to update
     * @param adminKey the adminKey of topic
     * @param submitKey the new submitKey for topic
     * @throws HieroException if the Topic could not be created
     */
    void updateSubmitKey(@NonNull TopicId topicId, @NonNull PrivateKey adminKey, @NonNull PrivateKey submitKey)
            throws HieroException;

    /**
     * Delete a Topic.
     *
     * @param topicId the topicId of topic
     * @throws HieroException if the Topic could not be created
     */
    void deleteTopic(@NonNull TopicId topicId) throws HieroException;

    /**
     * Delete a Topic.
     *
     * @param topicId the topicId of topic
     * @throws HieroException if the Topic could not be created
     */
    default void deleteTopic(@NonNull String topicId) throws HieroException {
        Objects.requireNonNull(topicId, "topicId cannot be null");
        deleteTopic(TopicId.fromString(topicId));
    };

    /**
     * Delete a Topic.
     *
     * @param topicId the topicId of topic
     * @throws HieroException if the Topic could not be created
     */
    void deleteTopic(@NonNull TopicId topicId, @NonNull PrivateKey adminKey) throws HieroException;

    /**
     * Delete a Topic.
     *
     * @param topicId the topicId of topic
     * @throws HieroException if the Topic could not be created
     */
    default void deleteTopic(@NonNull String topicId, @NonNull String adminKey) throws HieroException {
        Objects.requireNonNull(topicId, "topicId cannot be null");
        Objects.requireNonNull(adminKey, "adminKey cannot be null");
        deleteTopic(TopicId.fromString(topicId), PrivateKey.fromString(adminKey));
    };

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    void submitMessage(@NonNull TopicId topicId, @NonNull byte[] message) throws HieroException;

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    default void submitMessage(@NonNull String topicId, @NonNull byte[] message) throws HieroException {
        Objects.requireNonNull(topicId, "topicId cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
        submitMessage(TopicId.fromString(topicId), message);
    };

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    void submitMessage(@NonNull TopicId topicId, @NonNull String message) throws HieroException;

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    default void submitMessage(@NonNull String topicId, @NonNull String message) throws HieroException {
        Objects.requireNonNull(topicId, "topicId cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
        submitMessage(TopicId.fromString(topicId), message);
    };

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param submitKey the submit key for submitting message
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    void submitMessage(@NonNull TopicId topicId, @NonNull PrivateKey submitKey, @NonNull byte[] message)
            throws HieroException;

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    default void submitMessage(@NonNull String topicId, @NonNull String submitKey, @NonNull byte[] message)
            throws HieroException {
        Objects.requireNonNull(topicId, "topicId cannot be null");
        Objects.requireNonNull(submitKey, "submitKey cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
        submitMessage(TopicId.fromString(topicId), PrivateKey.fromString(submitKey), message);
    };

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param submitKey the submit key for submitting message
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    void submitMessage(@NonNull TopicId topicId, @NonNull PrivateKey submitKey, @NonNull String message)
            throws HieroException;

    /**
     * Submit a message to a Topic.
     *
     * @param topicId the topicId of topic
     * @param message the message to send to topic
     * @throws HieroException if the Topic could not be created
     */
    default void submitMessage(@NonNull String topicId, @NonNull String submitKey, @NonNull String message)
            throws HieroException {
        Objects.requireNonNull(topicId, "topicId cannot be null");
        Objects.requireNonNull(submitKey, "submitKey cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
        submitMessage(TopicId.fromString(topicId), PrivateKey.fromString(submitKey), message);
    };

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription)
            throws HieroException;

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    default SubscriptionHandle subscribeTopic(@NonNull String topicId, @NonNull Consumer<TopicMessage> subscription)
            throws HieroException {
        return subscribeTopic(TopicId.fromString(topicId), subscription);
    }

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @param limit the number of message to return
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                      long limit) throws HieroException;

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @param limit the number of message to return
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    default SubscriptionHandle subscribeTopic(@NonNull String topicId, @NonNull Consumer<TopicMessage> subscription,
                                              long limit) throws HieroException {
        return subscribeTopic(TopicId.fromString(topicId), subscription, limit);
    }

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @param startTime time to start subscribing to a topic
     * @param endTime time to stop subscribing to a topic
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                      @NonNull Instant startTime, @NonNull Instant endTime) throws HieroException;

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @param startTime time to start subscribing to a topic
     * @param endTime time to stop subscribing to a topic
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    default SubscriptionHandle subscribeTopic(@NonNull String topicId, @NonNull Consumer<TopicMessage> subscription,
                                              @NonNull Instant startTime, @NonNull Instant endTime) throws HieroException {
        return subscribeTopic(TopicId.fromString(topicId), subscription, startTime, endTime);
    }

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @param startTime time to start subscribing to a topic
     * @param endTime time to stop subscribing to a topic
     * @param limit the number of message to return
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    SubscriptionHandle subscribeTopic(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                      @NonNull Instant startTime, @NonNull Instant endTime, long limit)
            throws HieroException;

    /**
     * Subscribe to a Topic
     *
     * @param topicId the topicId of topic
     * @param startTime time to start subscribing to a topic
     * @param endTime time to stop subscribing to a topic
     * @param limit the number of message to return
     * @return SubscriptionHandle for the Topic
     * @throws HieroException if Topic could not be subscribed
     */
    default SubscriptionHandle subscribeTopic(@NonNull String topicId, @NonNull Consumer<TopicMessage> subscription,
                                              @NonNull Instant startTime, @NonNull Instant endTime, long limit)
            throws HieroException {
        return subscribeTopic(TopicId.fromString(topicId), subscription, startTime, endTime, limit);
    }
}
