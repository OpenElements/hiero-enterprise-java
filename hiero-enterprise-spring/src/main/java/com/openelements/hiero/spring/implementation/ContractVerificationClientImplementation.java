package com.openelements.hiero.spring.implementation;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.verification.ContractVerificationClient;
import com.openelements.hiero.base.verification.ContractVerificationState;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

public class ContractVerificationClientImplementation implements ContractVerificationClient {

    private static final String CONTRACT_VERIFICATION_URL = "https://server-verify.hashscan.io";

    private record VerifyRequest(String address, String chain, String creatorTxHash, String chosenContract,
                                 Map<String, String> files) {
    }

    private final HieroConfig hieroConfig;

    private final ObjectMapper objectMapper;

    private final RestClient restClient;

    public ContractVerificationClientImplementation(@NonNull final HieroConfig hieroConfig) {
        this.hieroConfig = Objects.requireNonNull(hieroConfig, "hieroConfig must not be null");
        objectMapper = new ObjectMapper();
        restClient = RestClient.create();
    }

    @NonNull
    private String getChainId() throws HieroException {
        return hieroConfig.chainId().map(id -> Long.toString(id))
                .orElseThrow(() -> new HieroException("Chain ID is not set"));
    }

    private void handleError(@NonNull final HttpRequest request, @NonNull final ClientHttpResponse response)
            throws IOException {
        Objects.requireNonNull(response, "response must not be null");
        final String error;
        try {
            final String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
                final JsonNode rootNode = objectMapper.readTree(body);
                final JsonNode errorNode = rootNode.get("error");
                if (errorNode != null) {
                    error = errorNode.asText();
                } else {
                    final JsonNode messageNode = rootNode.get("message");
                    if (messageNode != null) {
                        error = messageNode.asText();
                    } else {
                        error = body;
                    }
                }
            } catch (final Exception e) {
                throw new IOException("Error parsing body as JSON: " + body, e);
            }
        } catch (final Exception e) {
            throw new IOException("Error (" + response.getStatusCode() + "): " + response.getStatusText());
        }
        throw new IOException("Error (" + response.getStatusCode() + "): " + error);
    }

    @Override
    public ContractVerificationState verify(@NonNull final ContractId contractId, @NonNull final String contractName,
            @NonNull final Map<String, String> files) throws HieroException {
        Objects.requireNonNull(contractId, "contractId must not be null");
        Objects.requireNonNull(contractName, "contractName must not be null");
        Objects.requireNonNull(files, "files must not be null");

        final ContractVerificationState state = checkVerification(contractId);
        if (state != ContractVerificationState.NONE) {
            throw new IllegalStateException("Contract is already verified");
        }

        final VerifyRequest verifyRequest = new VerifyRequest(
                contractId.toSolidityAddress(),
                getChainId(),
                "",
                "",
                files
        );
        try {
            final String resultBody = restClient.post()
                    .uri(CONTRACT_VERIFICATION_URL + "/verify")
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(verifyRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        handleError(request, response);
                    }).body(String.class);
            final JsonNode rootNode = objectMapper.readTree(resultBody);
            final JsonNode resultNode = rootNode.get("result");
            if (resultNode != null) {
                if (resultNode.isArray()) {
                    final List<JsonNode> results = StreamSupport
                            .stream(resultNode.spliterator(), false)
                            .toList();
                    if (results.size() != 1) {
                        throw new RuntimeException("Expected exactly one result, got " + results.size());
                    }
                    final JsonNode result = results.get(0);
                    final JsonNode statusNode = result.get("status");
                    if (statusNode != null) {
                        if (statusNode.asText().equals("perfect")) {
                            return ContractVerificationState.FULL;
                        } else if (statusNode.asText().equals("false")) {
                            return ContractVerificationState.NONE;
                        } else {
                            throw new RuntimeException("Status is not success: " + statusNode.asText());
                        }
                    } else {
                        throw new RuntimeException("No status in response");
                    }
                } else {
                    throw new RuntimeException("Result is not an array");
                }
            } else {
                throw new RuntimeException("No result in response");
            }
        } catch (Exception e) {
            throw new HieroException("Error verification step", e);
        }
    }

    @Override
    public ContractVerificationState checkVerification(@NonNull final ContractId contractId) throws HieroException {
        Objects.requireNonNull(contractId, "contractId must not be null");

        final String uri =
                CONTRACT_VERIFICATION_URL + "/check-by-addresses" + "?addresses=" + contractId.toSolidityAddress()
                        + "&chainIds=" + getChainId();

        try {
            final String resultBody = restClient.get()
                    .uri(uri)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        handleError(request, response);
                    }).onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new RuntimeException(
                                "Server error (" + response.getStatusCode() + ") for request '" + request.getURI()
                                        + "'");
                    }).body(String.class);

            final JsonNode rootNode = objectMapper.readTree(resultBody);
            if (rootNode.isArray()) {
                final List<JsonNode> results = StreamSupport
                        .stream(rootNode.spliterator(), false)
                        .toList();
                if (results.size() != 1) {
                    throw new RuntimeException("Expected exactly one result, got " + results.size());
                }
                final JsonNode result = results.get(0);
                final JsonNode statusNode = result.get("status");
                if (statusNode != null) {
                    if (statusNode.asText().equals("perfect")) {
                        return ContractVerificationState.FULL;
                    } else if (statusNode.asText().equals("false")) {
                        return ContractVerificationState.NONE;
                    } else {
                        throw new RuntimeException("Status is not success: " + statusNode.asText());
                    }
                } else {
                    throw new RuntimeException("No status in response");
                }
            } else {
                throw new RuntimeException("Result is not an array");
            }
        } catch (Exception e) {
            throw new HieroException("Error verification step", e);
        }
    }

    @Override
    public boolean checkVerification(@NonNull final ContractId contractId, @NonNull final String fileName,
            @NonNull final String fileContent) throws HieroException {
        Objects.requireNonNull(contractId, "contractId must not be null");
        Objects.requireNonNull(fileName, "fileName must not be null");
        Objects.requireNonNull(fileContent, "fileContent must not be null");

        final ContractVerificationState state = checkVerification(contractId);
        if (state != ContractVerificationState.FULL) {
            throw new IllegalStateException("Contract is not verified");
        }

        final String uri = CONTRACT_VERIFICATION_URL + "/files/" + getChainId() + "/" + contractId.toSolidityAddress();

        try {
            final String resultBody = restClient.get()
                    .uri(uri)
                    .header("accept", "application/json")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        handleError(request, response);
                    }).body(String.class);

            final JsonNode rootNode = objectMapper.readTree(resultBody);
            if (rootNode.isArray()) {
                final List<JsonNode> results = StreamSupport
                        .stream(rootNode.spliterator(), false)
                        .filter(node -> node.get("name").asText().equals(fileName))
                        .toList();
                if (results.size() != 1) {
                    throw new RuntimeException("Expected exactly one result, got " + results.size());
                }
                final JsonNode result = results.get(0);
                final JsonNode contentNode = result.get("content");
                if (contentNode != null) {
                    return contentNode.asText().equals(fileContent);
                } else {
                    throw new RuntimeException("No content in response");
                }
            } else {
                throw new RuntimeException("Result is not an array");
            }
        } catch (Exception e) {
            throw new HieroException("Error verification step", e);
        }
    }
}
