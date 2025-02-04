package com.openelements.hiero.base.test;


import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.TokenBurnRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateResult;
import com.openelements.hiero.base.protocol.data.TokenMintRequest;
import com.openelements.hiero.base.protocol.data.TokenMintResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProtocolLayerClientTokenTests {

    private static HieroTestContext hieroTestContext;

    private static ProtocolLayerClient protocolLayerClient;

    @BeforeAll
    static void init() {
        hieroTestContext = new HieroTestContext();
        protocolLayerClient = new ProtocolLayerClientImpl(hieroTestContext);
    }

    @Test
    void testBurnNft() throws Exception {
        //given
        final TokenCreateRequest tokenCreateRequest = TokenCreateRequest.of("Test NFT", "TST",
                TokenType.NON_FUNGIBLE_UNIQUE,
                hieroTestContext.getOperatorAccount());
        final TokenCreateResult tokenCreateResult = protocolLayerClient.executeTokenCreateTransaction(
                tokenCreateRequest);
        final TokenId tokenId = tokenCreateResult.tokenId();

        final TokenMintRequest tokenMintRequest = TokenMintRequest.of(tokenId,
                hieroTestContext.getOperatorAccount().privateKey(), "https://example.com/metadata");
        final TokenMintResult tokenMintResult = protocolLayerClient.executeMintTokenTransaction(tokenMintRequest);
        final Long serial = tokenMintResult.serials().get(0);

        //when
        final TokenBurnRequest tokenBurnRequest = TokenBurnRequest.of(tokenId, serial,
                hieroTestContext.getOperatorAccount().privateKey());

        //then
        Assertions.assertDoesNotThrow(() -> protocolLayerClient.executeBurnTokenTransaction(tokenBurnRequest));
    }

}
