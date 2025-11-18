package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TopicMessage;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TopicMessageRequest(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                  @Nullable Instant startTime, @Nullable Instant endTime,
                                  long limit,
                                  Hbar queryPayment, Hbar maxQueryPayment) implements
        QueryRequest {

    private final static long NO_LIMIT = -1;

    public TopicMessageRequest {
        Objects.requireNonNull(topicId, "topicId must not be null");
        Objects.requireNonNull(subscription, "subscription must not be null");
    }

    @NonNull
    public static TopicMessageRequest of(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription) {
        return new TopicMessageRequest(topicId, subscription, null, null, NO_LIMIT, null, null);
    }

    @NonNull
    public static TopicMessageRequest of(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                         @NonNull long limit) {
        return new TopicMessageRequest(topicId, subscription, null, null, limit, null, null);
    }

    @NonNull
    public static TopicMessageRequest of(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                         @NonNull Instant startTime, @NonNull Instant endTime) {
        return new TopicMessageRequest(topicId, subscription, startTime, endTime, NO_LIMIT, null, null);
    }

    @NonNull
    public static TopicMessageRequest of(@NonNull TopicId topicId, @NonNull Consumer<TopicMessage> subscription,
                                         @NonNull Instant startTime, @NonNull Instant endTime, long limit) {
        return new TopicMessageRequest(topicId, subscription, startTime, endTime, limit, null, null);
    }
}
