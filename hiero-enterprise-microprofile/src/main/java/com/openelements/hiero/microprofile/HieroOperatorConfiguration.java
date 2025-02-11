package com.openelements.hiero.microprofile;

import jakarta.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "hiero")
@Dependent
public class HieroOperatorConfiguration {

    private String accountId;

    private String privateKey;
    
    public String getAccountId() {
        return accountId;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
