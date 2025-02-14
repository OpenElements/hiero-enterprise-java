package com.openelements.hiero.spring.sample;

import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.data.Account;
import java.util.Objects;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HieroEndpoint {

    private final AccountClient client;

    public HieroEndpoint(final AccountClient client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    @GetMapping("/")
    public String createAccount() {
        try {
            final Account account = client.createAccount();
            return "Account " + account.accountId() + " created!";
        } catch (final Exception e) {
            throw new RuntimeException("Error in Hedera call", e);
        }
    }
}
