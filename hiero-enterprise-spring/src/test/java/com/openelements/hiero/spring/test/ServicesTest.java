package com.openelements.hiero.spring.test;

import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.verification.ContractVerificationClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class ServicesTest {

  @Autowired private ProtocolLayerClient protocolLayerClient;

  @Autowired private ContractVerificationClient verificationClient;

  @Autowired private FileClient fileServiceClient;

  @Autowired private SmartContractClient smartContractServiceClient;

  @Test
  void testServices() throws Exception {
    Assertions.assertNotNull(protocolLayerClient);
    Assertions.assertNotNull(verificationClient);
    Assertions.assertNotNull(fileServiceClient);
    Assertions.assertNotNull(smartContractServiceClient);
  }
}
