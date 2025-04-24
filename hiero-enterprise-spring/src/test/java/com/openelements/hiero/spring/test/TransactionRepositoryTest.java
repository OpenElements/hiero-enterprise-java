package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.data.BalanceModification;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Result;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.mirrornode.TransactionRepository;
import com.openelements.hiero.base.protocol.data.TransactionType;
import com.openelements.hiero.test.HieroTestUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private HieroTestUtils hieroTestUtils;

    @Test
    void testFindTransactionByAccountId() throws HieroException {
        final Account account = accountClient.createAccount(1);
        hieroTestUtils.waitForMirrorNodeRecords();
        final Page<TransactionInfo> page = transactionRepository.findByAccount(account.accountId());
        Assertions.assertNotNull(page);

        final List<TransactionInfo> data = page.getData();
        Assertions.assertFalse(data.isEmpty());
    }

    @Test
    void testFindTransactionByAccountIdGiveEmptyListForAccountIdWithZeroTransaction() throws HieroException {
        final AccountId accountId = AccountId.fromString("0.0.0");
        hieroTestUtils.waitForMirrorNodeRecords();
        final Page<TransactionInfo> page = transactionRepository.findByAccount(accountId);
        Assertions.assertNotNull(page);

        final List<TransactionInfo> data = page.getData();
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    void testFindTransactionByAccountIdAndType() throws HieroException {
        final Account account = accountClient.createAccount(1);
        hieroTestUtils.waitForMirrorNodeRecords();
        final Page<TransactionInfo> page = transactionRepository.findByAccountAndType(account.accountId(),
                TransactionType.ACCOUNT_CREATE);
        Assertions.assertNotNull(page);
    }

    @Test
    void testFindTransactionByAccountIdAndResult() throws HieroException {
        final Account account = accountClient.createAccount(1);
        hieroTestUtils.waitForMirrorNodeRecords();
        final Page<TransactionInfo> page = transactionRepository.findByAccountAndResult(account.accountId(),
                Result.SUCCESS);
        Assertions.assertNotNull(page);
    }

    @Test
    void testFindTransactionByAccountIdAndBalanceModification() throws HieroException {
        final Account account = accountClient.createAccount(1);
        hieroTestUtils.waitForMirrorNodeRecords();
        final Page<TransactionInfo> page = transactionRepository.findByAccountAndModification(account.accountId(),
                BalanceModification.DEBIT);
        Assertions.assertNotNull(page);
    }
}
