package com.openelements.hiero.base.config.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.data.Account;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public class EnvBasedHieroConfig implements HieroConfig {

    private final AccountId operatorAccountId;
    private final PublicKey operatorPublicKey;
    private final PrivateKey operatorPrivateKey;
    private final String mirrorNodeAddress;

    private final String consensusNodeIp;
    private final String consensusNodePort;
    private final String consensusNodeAccount;

    private final String relayUrl;

    private final Long chainId;

    public EnvBasedHieroConfig() {
        operatorAccountId = getEnv("HEDERA_OPERATOR_ACCOUNT_ID")
                .map(AccountId::fromString)
                .orElseThrow(() -> new IllegalStateException("HEDERA_OPERATOR_ACCOUNT_ID is not set"));
        operatorPublicKey = getEnv("HEDERA_OPERATOR_PUBLIC_KEY")
                .map(PublicKey::fromString)
                .orElseThrow(() -> new IllegalStateException("HEDERA_OPERATOR_PUBLIC_KEY is not set"));
        operatorPrivateKey = getEnv("HEDERA_OPERATOR_PRIVATE_KEY")
                .map(PrivateKey::fromString)
                .orElseThrow(() -> new IllegalStateException("HEDERA_OPERATOR_PRIVATE_KEY is not set"));
        mirrorNodeAddress = getEnv("HEDERA_MIRROR_NODE_ADDRESS")
                .orElse(null);
        consensusNodeIp = getEnv("HEDERA_CONSENSUS_NODE_IP")
                .orElseThrow(() -> new IllegalStateException("HEDERA_CONSENSUS_NODE_IP is not set"));
        consensusNodePort = getEnv("HEDERA_CONSENSUS_NODE_PORT")
                .orElseThrow(() -> new IllegalStateException("HEDERA_CONSENSUS_NODE_PORT is not set"));
        consensusNodeAccount = getEnv("HEDERA_CONSENSUS_NODE_ACCOUNT")
                .orElseThrow(() -> new IllegalStateException("HEDERA_CONSENSUS_NODE_ACCOUNT is not set"));
        relayUrl = getEnv("HEDERA_RELAY_URL").orElse(null);
        chainId = getEnv("HEDERA_CHAIN_ID")
                .map(Long::valueOf)
                .orElse(null);
    }

    private Optional<String> getEnv(String key) {
        return Optional.ofNullable(System.getenv(key));
    }

    @Override
    public @NonNull Account getOperatorAccount() {
        return new Account(operatorAccountId, operatorPublicKey, operatorPrivateKey);
    }

    @Override
    public @NonNull Optional<String> getNetworkName() {
        return Optional.empty();
    }

    @Override
    public @NonNull Set<String> getMirrorNodeAddresses() {
        if (mirrorNodeAddress == null) {
            return Set.of();
        }
        return Set.of(mirrorNodeAddress);
    }

    @Override
    public @NonNull Set<ConsensusNode> getConsensusNodes() {
        ConsensusNode node = new ConsensusNode(
                consensusNodeIp,
                consensusNodePort,
                consensusNodeAccount
        );
        return Set.of(node);
    }

    @Override
    public @NonNull Optional<Long> chainId() {
        return Optional.ofNullable(chainId);
    }

    @Override
    public @NonNull Optional<String> relayUrl() {
        return Optional.ofNullable(relayUrl);
    }
}
