package com.test.domain;

public final class Alert {
  public static enum AlertCode {
    ERROR
  }

  public static enum Severity {
    AUDIT, INFO, WARN, ERROR
  }

  private final long timestamp;
  private final AlertCode code;
  private final Severity severity;
  private final String message;

  /**
   * Build an Alert.
   * 
   * @param timestamp
   *          The Timestamp
   * @param code
   *          The Alert code
   * @param message
   *          The Alert message
   */
  public Alert(long timestamp, AlertCode code, Severity severity, String message) {
    this.timestamp = timestamp;
    this.code = code;
    this.message = message;
    this.severity = severity;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public AlertCode getCode() {
    return this.code;
  }

  public Severity getSeverity() {
    return this.severity;
  }

  public String getMessage() {
    return this.message;
  }
}
