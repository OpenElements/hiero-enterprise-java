package com.openelements.hiero.base.test.config;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.config.NetworkSettings;
import com.openelements.hiero.base.data.Account;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

public class HieroTestContext implements HieroContext {

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(HieroTestContext.class);

    private final Account operationalAccount;

    private final Client client;

    private final Set<String> mirronNodeEndpoint;

    public HieroTestContext() {
        final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        final String hieroAccountIdByEnv = Optional.ofNullable(System.getenv("HEDERA_ACCOUNT_ID"))
                .orElse(dotenv.get("hiero.accountId"));
        final String hieroPrivateKeyByEnv = Optional.ofNullable(System.getenv("HEDERA_PRIVATE_KEY"))
                .orElse(dotenv.get("hiero.privateKey"));
        final String hieroNetwork = Optional.ofNullable(System.getenv("HEDERA_NETWORK"))
                .or(() -> Optional.ofNullable(dotenv.get("hiero.network")))
                .orElse("hedera-testnet");

        if (hieroAccountIdByEnv == null) {
            throw new IllegalStateException(
                    "AccountId for operator account is not set. Please set 'HEDERA_ACCOUNT_ID' or 'hiero.accountId' in .env file.");
        }
        if (hieroPrivateKeyByEnv == null) {
            throw new IllegalStateException(
                    "PrivateKey for operator account is not set. Please set 'HEDERA_PRIVATE_KEY' or 'hiero.privateKey' in .env file.");
        }

        log.info("Using operator account: {}", hieroAccountIdByEnv);
        log.info("Using network: {}", hieroNetwork);

        final AccountId accountId = AccountId.fromString(hieroAccountIdByEnv);
        final PrivateKey privateKey = PrivateKey.fromString(hieroPrivateKeyByEnv);
        final PublicKey publicKey = privateKey.getPublicKey();
        operationalAccount = new Account(accountId, publicKey, privateKey);
        
        final NetworkSettings networkSettings = NetworkSettings.forIdentifier(hieroNetwork)
                .orElseThrow(() -> new IllegalStateException("ENV 'HEDERA_NETWORK' is set to '" + hieroNetwork
                        + "' but no network settings are available for this network."));

        final Map<String, AccountId> nodes = new HashMap<>();
        networkSettings.getConsensusNodes()
                .forEach(consensusNode -> nodes.put(consensusNode.getAddress(), consensusNode.getAccountId()));
        mirronNodeEndpoint = networkSettings.getConsensusServiceAddress();
        client = Client.forNetwork(nodes);
        if (!networkSettings.getMirrorNodeAddresses().isEmpty()) {
            try {
                client.setMirrorNetwork(networkSettings.getMirrorNodeAddresses().stream().toList());
            } catch (InterruptedException e) {
                throw new RuntimeException("Error in configuring Mirror Node", e);
            }
        }
        client.setOperator(accountId, privateKey);
    }

    @Override
    public @NonNull Account getOperatorAccount() {
        return operationalAccount;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public @NonNull Set<String> getMirrorNodeEndPoint() {
        return mirronNodeEndpoint;
    }
}
