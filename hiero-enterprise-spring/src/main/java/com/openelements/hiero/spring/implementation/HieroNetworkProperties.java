package com.openelements.hiero.spring.implementation;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.hiero.network")
public class HieroNetworkProperties {

  /**
   * Network name (must be hedera-mainnet, hedera-testnet or hedera-previewnet). Default is
   * hedera-mainnet. Will be ignored if nodes are provided.
   */
  private String name = "hedera-mainnet";

  /**
   * List of nodes to connect to. If provided, network name will be ignored. This can be used to
   * connect to a custom network (like a Solo instance).
   */
  private List<HieroNode> nodes;

  /** Mirror node endpoint to connect to. */
  private String mirrorNode;

  private Long requestTimeoutInMs;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMirrorNode() {
    return mirrorNode;
  }

  public void setMirrorNode(String mirrorNode) {
    this.mirrorNode = mirrorNode;
  }

  public List<HieroNode> getNodes() {
    return nodes;
  }

  public void setNodes(List<HieroNode> nodes) {
    this.nodes = nodes;
  }

  public Long getRequestTimeoutInMs() {
    return requestTimeoutInMs;
  }

  public void setRequestTimeoutInMs(Long requestTimeoutInMs) {
    this.requestTimeoutInMs = requestTimeoutInMs;
  }
}
