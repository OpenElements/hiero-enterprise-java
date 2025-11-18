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

}
