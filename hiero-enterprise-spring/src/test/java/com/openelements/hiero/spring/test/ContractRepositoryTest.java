package com.openelements.hiero.spring.test;

import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Contract;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.mirrornode.ContractRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Test
    void testNullParam() {
        Assertions.assertThrows(NullPointerException.class, () -> contractRepository.findById((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> contractRepository.findByEvmAddress(null));
        Assertions.assertThrows(NullPointerException.class, () -> contractRepository.findByFileId(null));
        Assertions.assertThrows(NullPointerException.class, () -> contractRepository.findByProxyAccountId(null));
    }

    @Test
    void testFindAll() throws HieroException {
        // when
        final Page<Contract> contracts = contractRepository.findAll();

        // then
        Assertions.assertNotNull(contracts);
        Assertions.assertNotNull(contracts.getData());
    }

    @Test
    void testFindByIdWithNonExistentContract() throws HieroException {
        // given
        final String nonExistentContractId = "0.0.999999";

        // when
        final Optional<Contract> result = contractRepository.findById(nonExistentContractId);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testFindByEvmAddressWithNonExistentAddress() throws HieroException {
        // given
        final String nonExistentEvmAddress = "0x0000000000000000000000000000000000000000";

        // when
        final Page<Contract> result = contractRepository.findByEvmAddress(nonExistentEvmAddress);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
    }

    @Test
    void testFindByFileIdWithNonExistentFileId() throws HieroException {
        // given
        final String nonExistentFileId = "0.0.999999";

        // when
        final Page<Contract> result = contractRepository.findByFileId(nonExistentFileId);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
    }

    @Test
    void testFindByProxyAccountIdWithNonExistentAccountId() throws HieroException {
        // given
        final String nonExistentProxyAccountId = "0.0.999999";

        // when
        final Page<Contract> result = contractRepository.findByProxyAccountId(nonExistentProxyAccountId);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
    }
}
