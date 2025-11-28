package com.openelements.hiero.microprofile.implementation;

import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.implementation.MirrorNodeRestClient;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jspecify.annotations.NonNull;

public class MirrorNodeRestClientImpl implements MirrorNodeRestClient<JsonObject> {

  private final String target;

  public MirrorNodeRestClientImpl(String target) {
    this.target = target;
  }

  @Override
  public @NonNull JsonObject doGetCall(@NonNull String path) throws HieroException {
    Client client = ClientBuilder.newClient();
    Response response = client.target(target).path(path).request(MediaType.APPLICATION_JSON).get();

    if (response.getStatus() == 404 || !response.hasEntity()) {
      return JsonObject.EMPTY_JSON_OBJECT;
    }
    return response.readEntity(JsonObject.class);
  }

  public String getTarget() {
    return target;
  }
}
