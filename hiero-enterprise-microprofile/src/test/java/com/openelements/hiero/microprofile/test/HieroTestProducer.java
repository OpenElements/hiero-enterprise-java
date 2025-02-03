package com.openelements.hiero.microprofile.test;

import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.test.HieroTestUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class HieroTestProducer {

    @Produces
    @ApplicationScoped
    public HieroTestUtils createHieroTestUtils(
            MirrorNodeClient mirrorNodeClient, ProtocolLayerClient protocolLayerClient) {
        return new HieroTestUtils(mirrorNodeClient, protocolLayerClient);
    }
}
