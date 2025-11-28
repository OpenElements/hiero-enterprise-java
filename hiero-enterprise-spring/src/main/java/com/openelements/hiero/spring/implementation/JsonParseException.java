package com.openelements.hiero.spring.implementation;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonParseException extends IllegalStateException {

  public JsonParseException(JsonNode node) {
    super("Error in parsing JSON: " + node);
  }

  public JsonParseException(String message, JsonNode node) {
    super(message + " -> Error in parsing JSON: " + node);
  }

  public JsonParseException(JsonNode node, Throwable cause) {
    super("Error in parsing JSON: " + node, cause);
  }
}
