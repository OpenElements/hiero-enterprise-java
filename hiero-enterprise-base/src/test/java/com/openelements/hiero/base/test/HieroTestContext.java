package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.data.Account;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class HieroTestContext implements HieroContext {

    private final Account operationalAccount;

    private final Client client;

    public HieroTestContext() {
        final String hieroAccountIdByEnv = System.getenv("HEDERA_ACCOUNT_ID");
        final String hieroPrivateKeyByEnv = System.getenv("HEDERA_PRIVATE_KEY");
        final String hieroNetwork = System.getenv("HEDERA_NETWORK");

        if (hieroAccountIdByEnv != null && hieroPrivateKeyByEnv != null) {
            final AccountId accountId = AccountId.fromString(hieroAccountIdByEnv);
            final PrivateKey privateKey = PrivateKey.fromString(hieroPrivateKeyByEnv);
            final PublicKey publicKey = privateKey.getPublicKey();
            operationalAccount = new Account(accountId, publicKey, privateKey);
            if (Objects.equals(hieroNetwork, "testnet")) {
                client = Client.forTestnet();
                client.setOperator(accountId, privateKey);
            } else {
                //SOLO
                final Map<String, AccountId> nodes = new HashMap<>();
                nodes.put("127.0.0.1:50211", AccountId.fromString("0.0.3"));
                client = Client.forNetwork(nodes);
                try {
                    client.setMirrorNetwork(List.of("localhost:8080"));
                } catch (Exception e) {
                    throw new IllegalStateException("Error setting mirror network", e);
                }
                client.setOperator(accountId, privateKey);
                client.setRequestTimeout(Duration.ofSeconds(3));
            }
        } else {
            final Dotenv dotenv = Dotenv.load();
            final String accountIdAsString = dotenv.get("hiero.accountId");
            final String privateKeyAsString = dotenv.get("hiero.privateKey");
            final AccountId accountId = AccountId.fromString(accountIdAsString);
            final PrivateKey privateKey = PrivateKey.fromString(privateKeyAsString);
            final PublicKey publicKey = privateKey.getPublicKey();
            operationalAccount = new Account(accountId, publicKey, privateKey);
            client = Client.forTestnet();
            client.setOperator(accountId, privateKey);
        }
    }

    @Override
    public @NonNull Account getOperatorAccount() {
        return operationalAccount;
    }

    public Client getClient() {
        return client;
    }
}
