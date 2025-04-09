package com.openelements.hiero.base.mirrornode;

import com.hedera.hashgraph.sdk.TopicId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Topic;
import com.openelements.hiero.base.data.TopicMessage;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Interface for interacting with a Hiero network. This interface provides methods
 * for searching Topic and TopicMessages.
 */
public interface TopicRepository {
    /**
     * Return Topic for given topicId.
     *
     * @param topicId id of the topic
     * @return Optional of Topic
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<Topic> findTopicById(TopicId topicId) throws HieroException;

    /**
     * Return Topic for given topicId.
     *
     * @param topicId id of the topic
     * @return Optional of Topic
     * @throws HieroException if the search fails
     */
    @NonNull
    default Optional<Topic> findTopicById(String topicId) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        return findTopicById(TopicId.fromString(topicId));
    }

    /**
     * Return TopicMessages for given topicId.
     *
     * @param topicId id of the topic
     * @return Page of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    Page<TopicMessage> getMessages(TopicId topicId) throws HieroException;

    /**
     * Return TopicMessages for given topicId.
     *
     * @param topicId id of the topic
     * @return Page of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    default Page<TopicMessage> getMessages(String topicId) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        return getMessages(TopicId.fromString(topicId));
    };

    /**
     * Return TopicMessage for given topicId.
     *
     * @param topicId id of the topic
     * @param sequenceNumber sequenceNumber of the message
     * @return Optional of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<TopicMessage> getMessageBySequenceNumber(TopicId topicId, long sequenceNumber) throws HieroException;

    /**
     * Return TopicMessage for given topicId.
     *
     * @param topicId id of the topic
     * @param sequenceNumber sequenceNumber of the message
     * @return Optional of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    default Optional<TopicMessage> getMessageBySequenceNumber(String topicId, long sequenceNumber) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        return getMessageBySequenceNumber(TopicId.fromString(topicId), sequenceNumber);
    };
}
