package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.SubscriptionHandle;
import com.hedera.hashgraph.sdk.TransactionId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record TopicMessageResult(@NonNull SubscriptionHandle subscriptionHandle) {
    public TopicMessageResult {
        Objects.requireNonNull(subscriptionHandle, "subscriptionHandle must not be null");
    }
}
