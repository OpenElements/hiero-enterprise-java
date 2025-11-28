package com.openelements.hiero.microprofile.test;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConfigSource implements ConfigSource {

  private static final Logger log = LoggerFactory.getLogger(TestConfigSource.class);

  private final Map<String, String> properties;

  public TestConfigSource() {
    final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    properties = new HashMap<>();
    properties.put("mp.initializer.allow", "true");
    properties.put("mp.initializer.no-warn", "true");

    final String hieroAccountIdByEnv = System.getenv("HEDERA_ACCOUNT_ID");
    if (hieroAccountIdByEnv != null) {
      properties.put("hiero.accountId", hieroAccountIdByEnv);
    } else {
      properties.put("hiero.accountId", dotenv.get("hiero.accountId"));
    }

    final String hieroPrivateKeyByEnv = System.getenv("HEDERA_PRIVATE_KEY");
    if (hieroPrivateKeyByEnv != null) {
      properties.put("hiero.privateKey", hieroPrivateKeyByEnv);
    } else {
      properties.put("hiero.privateKey", dotenv.get("hiero.privateKey"));
    }

    final String hieroNetwork = System.getenv("HEDERA_NETWORK");
    if (hieroNetwork != null) {
      properties.put("hiero.network.name", hieroNetwork);
    } else {
      properties.put("hiero.network.name", dotenv.get("hiero.network.name"));
    }

    dotenv.entries().stream()
        .filter(e -> !e.getKey().equals("hiero.accountId"))
        .filter(e -> !e.getKey().equals("hiero.privateKey"))
        .filter(e -> !e.getKey().equals("hiero.network.name"))
        .forEach(e -> properties.put(e.getKey(), e.getValue()));

    properties.forEach((k, v) -> log.info("CONFIG: '" + k + "'->'" + v + "'"));
  }

  @Override
  public Set<String> getPropertyNames() {
    return properties.keySet();
  }

  @Override
  public String getValue(String propertyName) {
    return properties.get(propertyName);
  }

  @Override
  public String getName() {
    return TestConfigSource.class.getName();
  }
}
