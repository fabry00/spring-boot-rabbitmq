package com.test.domain;

import java.util.HashMap;
import java.util.Map;

public final class Header implements IHeader {

  private final String protocolVersion;
  private final String sender;
  private final long timestamp;
  private final String senderType;

  /**
   * Build the header.
   * 
   * @param protocolVersion
   *          The Version of the protocol
   * @param sender
   *          The component that sends the message
   * @param senderType
   *          The type of the component
   */
  public Header(String protocolVersion, String sender, String senderType) {
    this(protocolVersion, sender, System.currentTimeMillis(), senderType);
  }

  /**
   * Build the header.
   * 
   * @param protocolVersion
   *          The Version of the protocol
   * @param sender
   *          The component that sends the message
   * @param timestamp
   *          The timestamp of the message
   * @param senderType
   *          The type of the component
   */
  public Header(String protocolVersion, String sender, long timestamp, String senderType) {
    this.protocolVersion = protocolVersion;
    this.sender = sender;
    this.timestamp = timestamp;
    this.senderType = senderType;
  }

  @Override
  public String getProtocolVersion() {
    return this.protocolVersion;
  }

  @Override
  public String getSender() {
    return this.sender;
  }

  @Override
  public long getTimestamp() {
    return this.timestamp;
  }

  @Override
  public Map<String, Object> toMap() {
    Map<String, Object> headers = new HashMap<String, Object>();
    headers.put("protocolVersion", getProtocolVersion());
    headers.put("sender", getSender());
    headers.put("senderType", getSenderType());
    headers.put("timestamp", getTimestamp());
    return headers;
  }

  @Override
  public String getSenderType() {
    return senderType;
  }
}
