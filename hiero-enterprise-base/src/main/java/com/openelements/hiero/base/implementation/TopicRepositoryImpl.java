package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.TopicId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Topic;
import com.openelements.hiero.base.data.TopicMessage;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.mirrornode.TopicRepository;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class TopicRepositoryImpl implements TopicRepository {
  private final MirrorNodeClient mirrorNodeClient;

  public TopicRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
    this.mirrorNodeClient =
        Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
  }

  @Override
  public @NonNull Optional<Topic> findTopicById(TopicId topicId) throws HieroException {
    Objects.requireNonNull(topicId, "topicId must not be null");
    return mirrorNodeClient.queryTopicById(topicId);
  }

  @Override
  public @NonNull Page<TopicMessage> getMessages(TopicId topicId) throws HieroException {
    Objects.requireNonNull(topicId, "topicId must not be null");
    return mirrorNodeClient.queryTopicMessages(topicId);
  }

  @Override
  public @NonNull Optional<TopicMessage> getMessageBySequenceNumber(
      TopicId topicId, long sequenceNumber) throws HieroException {
    Objects.requireNonNull(topicId, "topicId must not be null");
    if (sequenceNumber < 1) {
      throw new IllegalArgumentException("sequenceNumber must be greater than 0");
    }
    return mirrorNodeClient.queryTopicMessageBySequenceNumber(topicId, sequenceNumber);
  }
}
