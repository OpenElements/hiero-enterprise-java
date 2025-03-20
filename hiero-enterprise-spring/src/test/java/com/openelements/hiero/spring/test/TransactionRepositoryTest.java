package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.*;
import com.openelements.hiero.base.data.*;
import com.openelements.hiero.base.mirrornode.TransactionRepository;
import com.openelements.hiero.base.protocol.data.TransactionType;
import com.openelements.hiero.test.HieroTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = TestConfig.class)
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private NftClient nftClient;

    @Autowired
    private TopicClient topicClient;

    @Autowired
    private HieroTestUtils hieroTestUtils;

    @Test
    void testFindTransactionByAccountId() throws HieroException {
        final Account account = accountClient.createAccount(10);
        final TokenId id = nftClient.createNftType("Hello Hiero", "NFT", account);
        nftClient.mintNft(id, account.privateKey(), "Hello Hiero".getBytes());
        hieroTestUtils.waitForMirrorNodeRecords();

        final Page<TransactionInfo> page = transactionRepository.findByAccount(account.accountId());
        Assertions.assertNotNull(page);
        final List<TransactionInfo> data = page.getData();
        System.out.println(page.getData().size());
        Assertions.assertFalse(data.isEmpty());
    }

    @Test
    void test2() throws HieroException {
        Page<TransactionInfo> page = transactionRepository.findByAccountAndResult("0.0.4951978", Result.FAIL);
        System.out.println(page.getData());
    }

    @Test
    void test3() throws HieroException {
        Page<TransactionInfo> page = transactionRepository.findByAccountAndModification("0.0.4951978", BalanceModification.DEBIT);
        System.out.println(page.getData());
    }
}
