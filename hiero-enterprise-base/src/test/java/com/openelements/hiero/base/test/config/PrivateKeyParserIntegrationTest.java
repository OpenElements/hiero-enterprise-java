package com.openelements.hiero.base.test.config;

import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hiero.base.config.PrivateKeyParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Integration test to verify private key parser works with real Hedera SDK integration.
 */
class PrivateKeyParserIntegrationTest {

    @Test
    void testIntegrationWithHederaSDK() {
        // Test that our parser produces keys that work with Hedera SDK operations
        // Using a known test vector
        final String testPrivateKeyDer = "302e020100300506032b657004220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37";
        final String testPrivateKeyHex = "c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37";
        
        // Parse both formats
        final PrivateKey fromDer = PrivateKeyParser.parsePrivateKey(testPrivateKeyDer);
        final PrivateKey fromHex = PrivateKeyParser.parsePrivateKey(testPrivateKeyHex);
        
        // Both should be valid private keys
        Assertions.assertNotNull(fromDer);
        Assertions.assertNotNull(fromHex);
        
        // Both should have valid public keys
        Assertions.assertNotNull(fromDer.getPublicKey());
        Assertions.assertNotNull(fromHex.getPublicKey());
        
        // DER format should work with direct SDK parsing for backward compatibility
        final PrivateKey directSdk = PrivateKey.fromString(testPrivateKeyDer);
        Assertions.assertEquals(directSdk.toString(), fromDer.toString());
    }
    
    @Test
    void testHexFormatsWithPrefix() {
        final String hexWithoutPrefix = "c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37";
        final String hexWithPrefix = "0x" + hexWithoutPrefix;
        
        final PrivateKey withoutPrefix = PrivateKeyParser.parsePrivateKey(hexWithoutPrefix);
        final PrivateKey withPrefix = PrivateKeyParser.parsePrivateKey(hexWithPrefix);
        
        // Both should produce the same result
        Assertions.assertEquals(withoutPrefix.toString(), withPrefix.toString());
        Assertions.assertEquals(withoutPrefix.getPublicKey().toString(), withPrefix.getPublicKey().toString());
    }
    
    @Test
    void testErrorHandling() {
        // Test that error messages are helpful
        final String invalidKey = "this-is-not-a-valid-key";
        
        final IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> PrivateKeyParser.parsePrivateKey(invalidKey)
        );
        
        // Error message should mention supported formats
        final String message = exception.getMessage();
        Assertions.assertTrue(message.contains("PEM") || message.contains("DER") || message.contains("hex"));
        Assertions.assertTrue(message.contains("format"));
    }
}