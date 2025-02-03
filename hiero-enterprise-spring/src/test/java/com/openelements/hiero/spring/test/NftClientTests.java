package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.NftClient;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class NftClientTests {

    @Autowired
    private NftClient nftClient;

    @Autowired
    private AccountClient accountClient;

    @Test
    void createNftType() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";

        //when
        final TokenId tokenId = nftClient.createNftType(name, symbol);

        //then
        Assertions.assertNotNull(tokenId);
    }

    @Test
    void testCreateNftForNullParam() {
        Assertions.assertThrows(
                NullPointerException.class, () -> nftClient.createNftType((String)null, null)
        );
        Assertions.assertThrows(
                NullPointerException.class, () -> nftClient.createNftType(null, null, (PrivateKey) null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> nftClient.createNftType(null, null, (AccountId) null, (PrivateKey) null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> nftClient.createNftType(null, null, null, null, (PrivateKey) null)
        );
    }

    @Test
    void mintNft() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final byte[] metadata = "https://example.com/metadata".getBytes(StandardCharsets.UTF_8);
        final TokenId tokenId = nftClient.createNftType(name, symbol);

        //when
        final long serial = nftClient.mintNft(tokenId, metadata);

        //then
        Assertions.assertTrue(serial > 0);
    }

    @Test
    void mintNfts() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final byte[] metadata1 = "https://example.com/metadata1".getBytes(StandardCharsets.UTF_8);
        final byte[] metadata2 = "https://example.com/metadata2".getBytes(StandardCharsets.UTF_8);
        final TokenId tokenId = nftClient.createNftType(name, symbol);

        //when
        final List<Long> serial = nftClient.mintNfts(tokenId, metadata1, metadata2);

        //then
        Assertions.assertEquals(2, serial.size());
    }

    @Test
    void associateNft() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final Account userAccount = accountClient.createAccount(1);

        //then
        Assertions.assertDoesNotThrow(() -> {
            nftClient.associateNft(tokenId, userAccount.accountId(), userAccount.privateKey());
        });
    }

    @Test
    void transferNft() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final Account treasuryAccount = accountClient.createAccount(1);
        final TokenId tokenId = nftClient.createNftType(name, symbol, treasuryAccount.accountId(),
                treasuryAccount.privateKey());
        final Account userAccount = accountClient.createAccount(1);
        final byte[] metadata = "https://example.com/metadata".getBytes(StandardCharsets.UTF_8);
        nftClient.associateNft(tokenId, userAccount.accountId(), userAccount.privateKey());
        final long serial = nftClient.mintNft(tokenId, treasuryAccount.privateKey(), metadata);

        //then
        Assertions.assertDoesNotThrow(() -> {
            nftClient.transferNft(tokenId, serial, treasuryAccount.accountId(), treasuryAccount.privateKey(),
                    userAccount.accountId());
        });
    }

    @Test
    void transferNftThrowsExceptionForInvalidTokenId() throws Exception {
        //given
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final Account treasuryAccount = accountClient.createAccount(1);
        final Account userAccount = accountClient.createAccount(1);
        final long serial = 1L;
        //then
        Assertions.assertThrows(HieroException.class, () -> nftClient.transferNft(tokenId, serial,
                treasuryAccount.accountId(), treasuryAccount.privateKey(), userAccount.accountId()));
        Assertions.assertThrows(HieroException.class, () -> nftClient.transferNfts(tokenId, List.of(serial),
                treasuryAccount.accountId(), treasuryAccount.privateKey(), userAccount.accountId()));
    }

    @Test
    void mintNftByNewUserAndTransferByAnotherUser() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final Account treasuryAccount = accountClient.createAccount();
        final Account supplierAccount = accountClient.createAccount();
        final TokenId tokenId = nftClient.createNftType(name, symbol, treasuryAccount, supplierAccount.privateKey());
        final byte[] metadata = "https://example.com/metadata".getBytes(StandardCharsets.UTF_8);

        final Account userAccount = accountClient.createAccount();
        nftClient.associateNft(tokenId, userAccount);

        final long serial = nftClient.mintNft(tokenId, supplierAccount.privateKey(), metadata);

        //then
        Assertions.assertDoesNotThrow(() -> {
            nftClient.transferNft(tokenId, serial, treasuryAccount, userAccount.accountId());
        });
    }
}
