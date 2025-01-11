package com.openelements.hiero.microprofile.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.FungibleTokenClient;
import com.openelements.hiero.base.NftClient;
import com.openelements.hiero.base.data.Token;
import com.openelements.hiero.base.data.TokenInfo;
import com.openelements.hiero.base.data.Balance;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.mirrornode.TokenRepository;
import com.openelements.hiero.microprofile.ClientProvider;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@HelidonTest
@AddBean(ClientProvider.class)
@Configuration(useExisting = true)
public class TokenRepositoryTest {
    @BeforeAll
    static void setup() {
        final Config build = ConfigProviderResolver.instance()
                .getBuilder().withSources(new TestConfigSource()).build();
        ConfigProviderResolver.instance().registerConfig(build, Thread.currentThread().getContextClassLoader());
    }

    @Inject
    private NftClient nftClient;

    @Inject
    private FungibleTokenClient tokenClient;

    @Inject
    private TokenRepository tokenRepository;

    @Inject
    private AccountClient accountClient;

    @Test
    void testNullParam() {
        Assertions.assertThrows(NullPointerException.class, () -> tokenRepository.findByAccount((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> tokenRepository.findById((String)null));
        Assertions.assertThrows(NullPointerException.class, () -> tokenRepository.getBalances((String)null));
        Assertions.assertThrows(NullPointerException.class, () -> tokenRepository.getBalancesForAccount(null, "1.2.3"));
        Assertions.assertThrows(NullPointerException.class, () -> tokenRepository.getBalancesForAccount("1.2.3", null));
        Assertions.assertThrows(NullPointerException.class, () -> tokenRepository.getBalancesForAccount((String)null, null));
    }

    @Test
    void testQueryTokenForAccount() throws Exception {
        // given
        final String name = "TOKEN";
        final String symbol = "TSY";
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        final PrivateKey privateKey = account.privateKey();

        final TokenId tokenId = tokenClient.createToken(name, symbol);
        tokenClient.associateToken(tokenId, newOwner, privateKey);
        //TODO: fix sleep
        Thread.sleep(10_000);

        // when
        final Page<Token> tokens = tokenRepository.findByAccount(newOwner);

        // then
        Assertions.assertNotNull(tokens);
        Assertions.assertTrue(!tokens.getData().isEmpty());
    }

    @Test
    void testQueryTokenForAccountReturnZeroResult() throws Exception {
        // given
        final String name = "TOKEN";
        final String symbol = "TSY";
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();

        final TokenId tokenId = tokenClient.createToken(name, symbol);
        //TODO: fix sleep
        Thread.sleep(10_000);

        // when
        final Page<Token> tokens = tokenRepository.findByAccount(newOwner);

        // then
        Assertions.assertNotNull(tokens);
        Assertions.assertTrue(tokens.getData().isEmpty());
    }

    @Test
    void testQueryTokenById() throws Exception {
        // given
        final String name = "TOKEN";
        final String symbol = "TSY";
        final TokenId fungiTokenId = tokenClient.createToken(name, symbol);
        final TokenId nftTokenId = nftClient.createNftType(name, symbol);
        //TODO: fix sleep
        Thread.sleep(10_000);

        // when
        final Optional<TokenInfo> fungiToken = tokenRepository.findById(fungiTokenId);
        final Optional<TokenInfo> nftToken = tokenRepository.findById(nftTokenId);


        // then
        Assertions.assertTrue(fungiToken.isPresent());
        Assertions.assertEquals(name, fungiToken.get().name());
        Assertions.assertEquals(symbol, fungiToken.get().symbol());
        Assertions.assertEquals(TokenType.FUNGIBLE_COMMON, fungiToken.get().type());

        Assertions.assertTrue(nftToken.isPresent());
        Assertions.assertEquals(name, nftToken.get().name());
        Assertions.assertEquals(symbol, nftToken.get().symbol());
        Assertions.assertEquals(TokenType.NON_FUNGIBLE_UNIQUE, nftToken.get().type());
    }

    @Test
    void testQueryTokenByIdReturnEmptyOptionalForInvalidId() throws Exception {
        // given
        final TokenId tokenId = TokenId.fromString("1.2.3");
        // when
        final Optional<TokenInfo> token = tokenRepository.findById(tokenId);
        // then
        Assertions.assertTrue(token.isEmpty());
    }


    @Test
    void testGetTokenBalances() throws Exception {
        // given
        final String name = "TOKEN";
        final String symbol = "TSY";
        final TokenId tokenId = tokenClient.createToken(name, symbol);
        //TODO: fix sleep
        Thread.sleep(10_000);

        // when
        final Page<Balance> balances = tokenRepository.getBalances(tokenId);

        // then
        Assertions.assertNotNull(balances.getData());
        Assertions.assertFalse(balances.getData().isEmpty());
    }

    @Test
    void testGetTokenBalancesReturnEmptyResultForInvalidId() throws Exception {
        // given
        final TokenId tokenId = TokenId.fromString("1.2.3");
        // when
        final Page<Balance> balances = tokenRepository.getBalances(tokenId);
        // then
        Assertions.assertEquals(0, balances.getData().size());
    }


    @Test
    void testGetTokenBalancesForAccount() throws Exception {
        // given
        final String name = "TOKEN";
        final String symbol = "TSY";
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        final PrivateKey newPrivateKey = account.privateKey();

        final TokenId tokenId = tokenClient.createToken(name, symbol);
        tokenClient.associateToken(tokenId, newOwner, newPrivateKey);
        //TODO: fix sleep
        Thread.sleep(10_000);

        // when
        final Page<Balance> balances = tokenRepository.getBalancesForAccount(tokenId, newOwner);

        // then
        Assertions.assertNotNull(balances.getData());
        Assertions.assertFalse(balances.getData().isEmpty());
    }

    @Test
    void testGetTokenBalancesForAccountReturnZeroResult() throws Exception {
        // given
        final String name = "TOKEN";
        final String symbol = "TSY";
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();

        final TokenId tokenId = tokenClient.createToken(name, symbol);
        //TODO: fix sleep
        Thread.sleep(10_000);

        // when
        final Page<Balance> balances = tokenRepository.getBalancesForAccount(tokenId, newOwner);

        // then
        Assertions.assertNotNull(balances.getData());
        Assertions.assertTrue(balances.getData().isEmpty());
    }
}

