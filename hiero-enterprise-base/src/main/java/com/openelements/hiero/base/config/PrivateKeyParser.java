package com.openelements.hiero.base.config;

import com.hedera.hashgraph.sdk.PrivateKey;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Pattern;
import org.jspecify.annotations.NonNull;

/**
 * Utility class for parsing private keys from various formats including DER, PEM, and hexadecimal.
 * This class provides enhanced private key support beyond the default Hedera SDK capabilities.
 * 
 * <p>Supported formats:
 * <ul>
 *   <li><strong>DER (binary)</strong>: Raw DER-encoded private key as hex string</li>
 *   <li><strong>PEM</strong>: PEM-formatted private key with BEGIN/END headers</li>
 *   <li><strong>Hex</strong>: Raw 32-byte private key as hexadecimal string</li>
 *   <li><strong>Legacy</strong>: Any format supported by Hedera SDK (for backward compatibility)</li>
 * </ul>
 * 
 * @since 0.21.0
 */
public final class PrivateKeyParser {

    private static final String PEM_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String PEM_FOOTER = "-----END PRIVATE KEY-----";
    private static final String PEM_EC_HEADER = "-----BEGIN EC PRIVATE KEY-----";
    private static final String PEM_EC_FOOTER = "-----END EC PRIVATE KEY-----";
    
    // Pattern for hex strings (with or without 0x prefix)
    private static final Pattern HEX_PATTERN = Pattern.compile("^(0x)?[0-9a-fA-F]+$");
    
    // Expected length for raw ECDSA private key (32 bytes = 64 hex characters)
    private static final int RAW_PRIVATE_KEY_LENGTH = 64;

    private PrivateKeyParser() {
        // Utility class - prevent instantiation
    }

    /**
     * Parses a private key from various supported formats.
     * 
     * @param privateKeyString the private key string in any supported format
     * @return the parsed PrivateKey instance
     * @throws IllegalArgumentException if the private key cannot be parsed or is in an unsupported format
     * @throws NullPointerException if privateKeyString is null
     */
    @NonNull
    public static PrivateKey parsePrivateKey(@NonNull final String privateKeyString) {
        Objects.requireNonNull(privateKeyString, "privateKeyString must not be null");
        
        final String trimmed = privateKeyString.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Private key string cannot be empty");
        }

        try {
            // Try to detect and parse the format
            if (isPemFormat(trimmed)) {
                return parseFromPem(trimmed);
            } else if (isHexFormat(trimmed)) {
                return parseFromHex(trimmed);
            } else {
                // Fall back to default Hedera SDK parsing (for DER and other formats)
                return parseFromDefault(trimmed);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Cannot parse private key. Supported formats: PEM, DER (hex), raw hex (32 bytes). " +
                "Provided key format: " + detectFormat(trimmed) + 
                ". Error: " + e.getMessage(), e);
        }
    }

    /**
     * Detects the format of the private key string for error reporting.
     * 
     * @param privateKeyString the private key string
     * @return a human-readable description of the detected format
     */
    @NonNull
    public static String detectFormat(@NonNull final String privateKeyString) {
        Objects.requireNonNull(privateKeyString, "privateKeyString must not be null");
        
        final String trimmed = privateKeyString.trim();
        
        if (isPemFormat(trimmed)) {
            return "PEM format";
        } else if (isHexFormat(trimmed)) {
            final String cleanHex = trimmed.startsWith("0x") || trimmed.startsWith("0X") ? trimmed.substring(2) : trimmed;
            if (cleanHex.length() == RAW_PRIVATE_KEY_LENGTH) {
                return "Raw hex format (32 bytes)";
            } else {
                return "Hex format (" + cleanHex.length()/2 + " bytes)";
            }
        } else {
            return "DER format";
        }
    }

    /**
     * Checks if the private key string is in PEM format.
     */
    private static boolean isPemFormat(@NonNull final String privateKeyString) {
        return privateKeyString.contains(PEM_HEADER) || privateKeyString.contains(PEM_EC_HEADER);
    }

    /**
     * Checks if the private key string is in hexadecimal format.
     * Only considers it hex format if it's exactly 64 characters (32 bytes) or has 0x prefix.
     */
    private static boolean isHexFormat(@NonNull final String privateKeyString) {
        if (!HEX_PATTERN.matcher(privateKeyString).matches()) {
            return false;
        }
        
        String cleanHex = privateKeyString;
        if (cleanHex.startsWith("0x") || cleanHex.startsWith("0X")) {
            cleanHex = cleanHex.substring(2);
        }
        
        // Only consider it hex format if it's exactly 64 characters (32 bytes) or has 0x prefix
        return cleanHex.length() == RAW_PRIVATE_KEY_LENGTH || privateKeyString.startsWith("0x") || privateKeyString.startsWith("0X");
    }

    /**
     * Parses a private key from PEM format.
     */
    @NonNull
    private static PrivateKey parseFromPem(@NonNull final String pemString) {
        try {
            // Extract the base64 content between headers and footers
            String base64Content = pemString;
            
            // Handle standard PKCS#8 PEM format
            if (pemString.contains(PEM_HEADER)) {
                base64Content = extractBase64Content(pemString, PEM_HEADER, PEM_FOOTER);
            }
            // Handle EC private key PEM format
            else if (pemString.contains(PEM_EC_HEADER)) {
                base64Content = extractBase64Content(pemString, PEM_EC_HEADER, PEM_EC_FOOTER);
            }
            
            // Remove any whitespace and newlines
            base64Content = base64Content.replaceAll("\\s+", "");
            
            // Try to parse as DER after base64 decoding
            final byte[] derBytes = java.util.Base64.getDecoder().decode(base64Content);
            final String derHex = bytesToHex(derBytes);
            
            return PrivateKey.fromString(derHex);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid PEM format: " + e.getMessage(), e);
        }
    }

    /**
     * Parses a private key from hexadecimal format.
     */
    @NonNull
    private static PrivateKey parseFromHex(@NonNull final String hexString) {
        try {
            String cleanHex = hexString;
            
            // Remove 0x prefix if present
            if (cleanHex.startsWith("0x") || cleanHex.startsWith("0X")) {
                cleanHex = cleanHex.substring(2);
            }
            
            // Validate length for raw private key
            if (cleanHex.length() == RAW_PRIVATE_KEY_LENGTH) {
                // This is likely a raw 32-byte private key
                return PrivateKey.fromString(cleanHex);
            } else {
                // This might be a DER-encoded key in hex format
                return PrivateKey.fromString(cleanHex);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid hex format: " + e.getMessage(), e);
        }
    }

    /**
     * Falls back to the default Hedera SDK parsing.
     */
    @NonNull
    private static PrivateKey parseFromDefault(@NonNull final String privateKeyString) {
        return PrivateKey.fromString(privateKeyString);
    }

    /**
     * Extracts base64 content between PEM headers and footers.
     */
    @NonNull
    private static String extractBase64Content(@NonNull final String pemString, 
                                             @NonNull final String header, 
                                             @NonNull final String footer) {
        final int headerIndex = pemString.indexOf(header);
        final int footerIndex = pemString.indexOf(footer);
        
        if (headerIndex == -1 || footerIndex == -1 || footerIndex <= headerIndex) {
            throw new IllegalArgumentException("Invalid PEM format: missing or malformed headers");
        }
        
        return pemString.substring(headerIndex + header.length(), footerIndex).trim();
    }

    /**
     * Converts byte array to hexadecimal string.
     */
    @NonNull
    private static String bytesToHex(@NonNull final byte[] bytes) {
        final StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}