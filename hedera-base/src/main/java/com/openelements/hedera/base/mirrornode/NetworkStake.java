package com.openelements.hedera.base.mirrornode;

public record NetworkStake(
        long maxStakeReward,
        long maxStakeRewardPerHbar,
        long maxTotalReward,
        double nodeRewardFeeFraction,
        long reservedStakingRewards,
        long rewardBalanceThreshold,
        long stakeTotal,
        long stakingPeriodDuration,
        long stakingPeriodsStored,
        double stakingRewardFeeFraction,
        long stakingRewardRate,
        long stakingStartThreshold,
        long unreservedStakingRewardBalance
) {
    public NetworkStake {}
}
