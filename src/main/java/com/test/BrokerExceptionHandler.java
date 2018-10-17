package com.test;

import com.test.domain.Alert;
import com.test.domain.Alert.AlertCode;
import com.test.domain.Alert.Severity;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ErrorHandler;

public class BrokerExceptionHandler implements ErrorHandler {

  private final Logger logger = Logger.getLogger(getClass().getSimpleName());

  @Autowired
  private Helper helper;

  @Override
  public void handleError(Throwable t) {
    logger.log(Level.SEVERE, "Exception Detected. Publishing error alert");
    final Alert alert = new Alert(System.currentTimeMillis(), AlertCode.ERROR, Severity.ERROR,
        "Exception detected. Message: " + t.getMessage());

    System.out.println("---> Before publishing Alert event");
    helper.publishAlert(alert);
    System.out.println("---> After publishing Alert event");
    // throw new AmqpRejectAndDontRequeueException("Error Handler converted
    // exception to fatal", t);

  }
}