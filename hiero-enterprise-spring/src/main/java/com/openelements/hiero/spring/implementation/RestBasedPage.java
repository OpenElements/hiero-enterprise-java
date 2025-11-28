package com.openelements.hiero.spring.implementation;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openelements.hiero.base.data.Page;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class RestBasedPage<T> implements Page<T> {

  private static final Logger log = LoggerFactory.getLogger(RestBasedPage.class);

  private final ObjectMapper objectMapper;

  private final RestClient restClient;

  private final Function<JsonNode, List<T>> dataExtractionFunction;

  private final int number;

  private final List<T> data;

  private final String nextPath;

  private final String rootPath;

  private final String currentPath;

  public RestBasedPage(
      final @NonNull ObjectMapper objectMapper,
      final RestClient.Builder restClient,
      final @NonNull String path,
      final @NonNull Function<JsonNode, List<T>> dataExtractionFunction) {
    this(objectMapper, restClient, path, 0, dataExtractionFunction, path);
  }

  public RestBasedPage(
      final @NonNull ObjectMapper objectMapper,
      final RestClient.Builder restClientBuilder,
      final @NonNull String path,
      int number,
      final @NonNull Function<JsonNode, List<T>> dataExtractionFunction,
      final @NonNull String rootPath) {
    this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    Objects.requireNonNull(restClientBuilder, "restClientBuilder must not be null");
    this.dataExtractionFunction =
        Objects.requireNonNull(dataExtractionFunction, "dataExtractionFunction must not be null");
    this.rootPath = Objects.requireNonNull(rootPath, "rootPath must not be null");
    this.currentPath = Objects.requireNonNull(path, "path must not be null");
    this.number = number;
    if (number < 0) {
      throw new IllegalArgumentException("number must be non-negative");
    }
    log.debug("Fetching data from PATH: {}", path);
    restClient = restClientBuilder.build();
    String[] pathParts = path.split("\\?");
    final String requestPath = pathParts[0];
    final String requestQuery;
    if (pathParts.length > 1) {
      requestQuery = pathParts[1];
    } else {
      requestQuery = null;
    }

    final ResponseEntity<String> response =
        restClient
            .get()
            .uri(uriBuilder -> uriBuilder.path(requestPath).query(requestQuery).build())
            .accept(APPLICATION_JSON)
            .retrieve()
            .toEntity(String.class);
    final HttpStatusCode statusCode = response.getStatusCode();
    if (!statusCode.is2xxSuccessful()) {
      throw new IllegalStateException("HTTP status code: " + statusCode);
    }
    final String body = response.getBody();
    if (body == null) {
      throw new IllegalStateException("Response body is null");
    }
    try {
      final JsonNode jsonNode = objectMapper.readTree(body);
      data = Collections.unmodifiableList(dataExtractionFunction.apply(jsonNode));
      nextPath = getNextPath(jsonNode);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JSON parsing error", e);
    }
  }

  private String getNextPath(final JsonNode jsonNode) {
    if (!jsonNode.has("links")) {
      return null;
    }
    final JsonNode linksNode = jsonNode.get("links");
    if (linksNode.isNull()) {
      return null;
    }
    if (!linksNode.has("next")) {
      return null;
    }
    final JsonNode nextNode = linksNode.get("next");
    if (nextNode.isNull()) {
      return null;
    }
    if (!nextNode.isTextual()) {
      throw new IllegalArgumentException("Next link is not a string: " + nextNode);
    }
    try {
      return nextNode.asText();
    } catch (Exception e) {
      throw new IllegalArgumentException("Error parsing next link '" + nextNode.asText() + "'", e);
    }
  }

  @Override
  public int getPageIndex() {
    return number;
  }

  @Override
  public int getSize() {
    return data.size();
  }

  @Override
  public List<T> getData() {
    return data;
  }

  @Override
  public boolean hasNext() {
    return nextPath != null;
  }

  @Override
  public Page<T> next() {
    if (nextPath == null) {
      throw new IllegalStateException("No next Page");
    }
    return new RestBasedPage<>(
        objectMapper,
        restClient.mutate().clone(),
        nextPath,
        number + 1,
        dataExtractionFunction,
        rootPath);
  }

  @Override
  public Page<T> first() {
    return new RestBasedPage<>(
        objectMapper, restClient.mutate().clone(), rootPath, dataExtractionFunction);
  }

  @Override
  public boolean isFirst() {
    return Objects.equals(rootPath, currentPath);
  }
}
