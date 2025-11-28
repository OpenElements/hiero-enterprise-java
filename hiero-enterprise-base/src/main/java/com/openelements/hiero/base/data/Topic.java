package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.TopicId;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record Topic(
    @NonNull TopicId topicId,
    @Nullable PublicKey adminKey,
    @Nullable AccountId autoRenewAccount,
    int autoRenewPeriod,
    @NonNull Instant createdTimestamp,
    @NonNull List<FixedFee> fixedFees,
    @Nullable List<PublicKey> feeExemptKeyList,
    @Nullable PublicKey feeScheduleKey,
    @Nullable PublicKey submitKey,
    boolean deleted,
    String memo,
    @NonNull Instant fromTimestamp,
    @NonNull Instant toTimestamp) {
  public Topic {
    Objects.requireNonNull(topicId, "topicId must not be null");
    Objects.requireNonNull(createdTimestamp, "createdTimestamp must not be null");
    Objects.requireNonNull(fixedFees, "fixedFees must not be null");
    Objects.requireNonNull(fromTimestamp, "fromTimestamp must not be null");
    Objects.requireNonNull(toTimestamp, "toTimestamp must not be null");
  }
}
