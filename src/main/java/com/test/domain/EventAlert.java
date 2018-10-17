package com.test.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class EventAlert {
  private static final String EVENT_ID = "myevent.ALERT";
  public static final String ENCODING = "UTF-8";

  public String getEventId() {
    return EVENT_ID;
  }

  public String encode(Alert alert) {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(alert);
  }

  public Alert decode(String encodedMessage) {
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(encodedMessage, Alert.class);
  }

}
