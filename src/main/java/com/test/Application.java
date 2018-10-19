package com.test;

import com.test.domain.Alert;
import com.test.domain.EventAlert;
import com.test.domain.EventKpisCollected;
import com.test.domain.KpisMessage;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  Logger logger = Logger.getLogger(Application.class.getSimpleName());

  /**
   * Main.
   * 
   * @param args
   *          Arguments.
   * @throws InterruptedException
   *           exception.
   */
  public static void main(String[] args) throws InterruptedException {
    System.out.println(Application.class.getName() + " is starting");
    SpringApplication.run(Application.class, args);
  }

  public void kpisEvent(KpisMessage message) throws UnsupportedEncodingException {
    logger.log(Level.INFO, "---> kpisEvent RECEIVED");

    //final String messageRaw = new String(body, EventKpisCollected.ENCODING);

   // KpisMessage message = new EventKpisCollected().decode(messageRaw);

    logger.log(Level.INFO, "---> kpisEvent DECODED, windowId: " + message.getWindowId());
  }

  public void alertEvent(byte[] body) throws UnsupportedEncodingException {
    logger.log(Level.INFO, "---> kpisAlert RECEIVED");

    final String messageRaw = new String(body, EventAlert.ENCODING);

    Alert alert = new EventAlert().decode(messageRaw);

    logger.log(Level.SEVERE, "---> Message: " + alert.getMessage());
  }
}
