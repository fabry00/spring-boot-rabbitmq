package com.test.domain;

import java.util.Map;

public interface IHeader {

  public String getProtocolVersion();

  public String getSender();

  public String getSenderType();

  public long getTimestamp();

  public Map<String, Object> toMap();
}
