package com.openelements.hiero.spring.test;

import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.mirrornode.NetworkRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class NetworkRepositoryTest {
    @Autowired
    private NetworkRepository networkRepository;

    @Test
    void findExchangeRates() throws HieroException {
        Optional<ExchangeRates> result = networkRepository.exchangeRates();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void findNetworkFees() throws HieroException {
        List<NetworkFee> result = networkRepository.fees();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void findNetworkStake() throws HieroException {
        Optional<NetworkStake> result = networkRepository.stake();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @Disabled
    void findNetworkSupplies() throws HieroException {
        Optional<NetworkSupplies> result = networkRepository.supplies();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }
}
