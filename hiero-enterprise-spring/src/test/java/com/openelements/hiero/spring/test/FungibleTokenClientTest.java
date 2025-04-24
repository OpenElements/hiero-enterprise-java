package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.FungibleTokenClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class FungibleTokenClientTest {

    @Autowired
    private FungibleTokenClient tokenClient;

    @Autowired
    private AccountClient accountClient;

    @Test
    void createToken() throws HieroException {
        final String name = "TOKEN";
        final String symbol = "FT";

        final TokenId tokenId = tokenClient.createToken(name, symbol);

        Assertions.assertNotNull(tokenId);
    }

    @Test
    void associateToken() throws HieroException {
        final String name = "TOKEN";
        final String symbol = "FT";
        final TokenId tokenId = tokenClient.createToken(name, symbol);

        final Account account = accountClient.createAccount(1);

        Assertions.assertDoesNotThrow(() -> tokenClient.associateToken(tokenId, account));
    }

    @Test
    void mintToken() throws HieroException {
        final String name = "TOKEN";
        final String symbol = "FT";
        final Long amount = 1L;

        final TokenId tokenId = tokenClient.createToken(name, symbol);
        final long totalSupply = tokenClient.mintToken(tokenId, amount);

        Assertions.assertEquals(amount, totalSupply);
    }

    @Test
    void burnToken() throws HieroException {
        final String name = "TOKEN";
        final String symbol = "FT";
        final long amount = 1L;

        final TokenId tokenId = tokenClient.createToken(name, symbol);
        tokenClient.mintToken(tokenId, amount);

        final long supplyTotal = tokenClient.burnToken(tokenId, 1L);
        Assertions.assertEquals(0, supplyTotal);
    }

    @Test
    void transferToken() throws HieroException {
        final Account toAccount = accountClient.createAccount(1);
        final String name = "TOKEN";
        final String symbol = "FT";

        final TokenId tokenId = tokenClient.createToken(name, symbol);
        tokenClient.associateToken(tokenId, toAccount);

        long totalSupply = tokenClient.mintToken(tokenId, 1L);

        Assertions.assertDoesNotThrow(() -> tokenClient.transferToken(tokenId, toAccount.accountId(), totalSupply));
    }
}
