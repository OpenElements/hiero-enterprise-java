package com.openelements.hiero.microprofile.test;

import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.microprofile.ClientProvider;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@HelidonTest
@AddBean(ClientProvider.class)
@Configuration(useExisting = true)
public class FileClientTests {

  @BeforeAll
  static void setup() {
    final Config build =
        ConfigProviderResolver.instance().getBuilder().withSources(new TestConfigSource()).build();
    ConfigProviderResolver.instance()
        .registerConfig(build, Thread.currentThread().getContextClassLoader());
  }

  @Inject private FileClient fileClient;

  @Test
  void testFileClient() throws Exception {
    // given
    final byte[] contents = "Hello, Hedera!".getBytes();
    Assertions.assertNotNull(fileClient);

    // when
    final FileId fileId = fileClient.createFile(contents);

    // then
    Assertions.assertNotNull(fileId);
  }
}
