package com.openelements.hiero.spring.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.implementation.MirrorNodeRestClient;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

public class MirrorNodeRestClientImpl implements MirrorNodeRestClient<JsonNode> {

  private final ObjectMapper objectMapper;

  private final RestClient restClient;

  public MirrorNodeRestClientImpl(final RestClient.Builder restClientBuilder) {
    Objects.requireNonNull(restClientBuilder, "restClientBuilder must not be null");
    objectMapper = new ObjectMapper();
    restClient = restClientBuilder.build();
  }

  public JsonNode doGetCall(String path) throws HieroException {
    return doGetCall(builder -> builder.path(path).build());
  }

  public JsonNode doGetCall(Function<UriBuilder, URI> uriFunction) throws HieroException {
    final ResponseEntity<String> responseEntity =
        restClient
            .get()
            .uri(uriBuilder -> uriFunction.apply(uriBuilder))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                (request, response) -> {
                  if (!HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
                    throw new RuntimeException("Client error: " + response.getStatusText());
                  }
                })
            .onStatus(
                HttpStatusCode::is5xxServerError,
                (request, response) -> {
                  throw new RuntimeException("Server error: " + response.getStatusText());
                })
            .toEntity(String.class);
    final String body = responseEntity.getBody();
    try {
      if (HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode())) {
        return objectMapper.readTree("{}");
      }
      if (body == null || body.isBlank()) {
        return objectMapper.readTree("{}");
      }
      return objectMapper.readTree(body);
    } catch (JsonProcessingException e) {
      throw new HieroException("Error parsing body as JSON: " + body, e);
    }
  }
}
