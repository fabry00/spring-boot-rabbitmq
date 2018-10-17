package com.test.domain;

import java.util.Collections;
import java.util.List;

public final class KpisMessage {
  private final List<String> kpis;
  private final String windowId;

  /** A Generic message containing KPIs. */
  public KpisMessage(List<String> kpis, String windowId) {
    this.kpis = Collections.unmodifiableList(kpis);
    this.windowId = windowId;
  }

  public List<String> getKpis() {
    return kpis;
  }

  public String getWindowId() {
    return windowId;
  }
}
