package com.test.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public final class EventKpisCollected {
  private static final String EVENT_ID = "mydomain.KPIS";
  public static final String ENCODING = "UTF-8";

  public String getEventId() {
    return EVENT_ID;
  }

  public String encode(List<String> kpis, String windowId) {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(new KpisMessage(kpis, windowId));
  }

  /**
   * GenerikKpisMessage decoder.
   * 
   * @param encodedMessage
   *          The encoded message.
   * @return GenericKpisMessage
   */
  public KpisMessage decode(String encodedMessage) {
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(encodedMessage, KpisMessage.class);
  }
}