package com.openelements.hiero.spring.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.config.NetworkSettings;
import com.openelements.hiero.base.data.Account;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;

public class HieroConfigImpl implements HieroConfig {

    private final Account operatorAccount;

    private final String networkName;

    private final Set<String> mirrorNodeAddresses;

    private final Set<ConsensusNode> consensusNodes;

    private final Long chainId;

    private final String relayUrl;

    public HieroConfigImpl(@NonNull final HieroProperties properties) {
        Objects.requireNonNull(properties, "properties must not be null");

        Objects.requireNonNull(properties.getPrivateKey(), "privateKey must not be null");
        final AccountId operatorAccountId = parseAccountId(properties.getAccountId());

        Objects.requireNonNull(properties.getAccountId(), "accountId must not be null");
        final PrivateKey operatorPrivateKey = parsePrivateKey(properties.getPrivateKey());

        operatorAccount = Account.of(operatorAccountId, operatorPrivateKey);

        final Optional<NetworkSettings> networkSettings = NetworkSettings.forIdentifier(
                properties.getNetwork().getName());
        if (networkSettings.isPresent()) {
            final NetworkSettings settings = networkSettings.get();
            networkName = settings.getNetworkName().orElse(properties.getNetwork().getName());
            mirrorNodeAddresses = Collections.unmodifiableSet(settings.getMirrorNodeAddresses());
            consensusNodes = Collections.unmodifiableSet(settings.getConsensusNodes());
            chainId = settings.chainId().orElse(null);
            relayUrl = settings.relayUrl().orElse(null);
        } else {
            networkName = properties.getNetwork().getName();
            final String mirrorNodeAddress = properties.getNetwork().getMirrorNode();
            if (mirrorNodeAddress != null && !mirrorNodeAddress.isBlank()) {
                mirrorNodeAddresses = Set.of(mirrorNodeAddress);
            } else {
                mirrorNodeAddresses = Set.of();
            }
            final List<HieroNode> nodes = properties.getNetwork().getNodes();
            if (nodes == null || nodes.isEmpty()) {
                consensusNodes = Set.of();
            } else {
                consensusNodes = properties.getNetwork().getNodes().stream()
                        .map(node -> new ConsensusNode(node.getIp(), node.getPort() + "", node.getAccount()))
                        .collect(Collectors.toUnmodifiableSet());
            }
            chainId = null;
            relayUrl = null;
        }
    }

    private static AccountId parseAccountId(final String accountId) {
        try {
            return AccountId.fromString(accountId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'accountId' property: '" + accountId + "'", e);
        }
    }

    private static PrivateKey parsePrivateKey(final String privateKey) {
        try {
            return PrivateKey.fromString(privateKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'privateKey' property: '" + privateKey + "'", e);
        }
    }


    @Override
    public Account getOperatorAccount() {
        return operatorAccount;
    }

    @Override
    public Optional<String> getNetworkName() {
        return Optional.ofNullable(networkName);
    }

    @Override
    public Set<String> getMirrorNodeAddresses() {
        return mirrorNodeAddresses;
    }

    @Override
    public Set<ConsensusNode> getConsensusNodes() {
        return consensusNodes;
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
