package com.openelements.hiero.spring.test;

import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.spring.EnableHiero;
import com.openelements.hiero.test.HieroTestUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableHiero
@SpringBootConfiguration
@ComponentScan
public class TestConfig {

    @Bean
    HieroTestUtils hieroTestUtils(MirrorNodeClient mirrorNodeClient, ProtocolLayerClient protocolLayerClient) {
        HieroTestUtils testUtils = new HieroTestUtils(mirrorNodeClient, protocolLayerClient);
        return testUtils;
    }
}
