package com.openelements.hiero.microprofile.sample;

import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.data.Account;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class HieroEndpoint {

  private final AccountClient client;

  @Inject
  public HieroEndpoint(final AccountClient client) {
    this.client = client;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String createAccount() {
    try {
      final Account account = client.createAccount();
      return "Account created!";
    } catch (final Exception e) {
      throw new RuntimeException("Error in Hedera call", e);
    }
  }
}
