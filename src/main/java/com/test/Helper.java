package com.test;

import com.test.domain.Alert;
import com.test.domain.IHeader;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Helper {
  private final Logger logger = Logger.getLogger(Helper.class.getSimpleName());

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Value("${spring.application.name}")
  private String applicationName;

  private String protocolVersion = "1.0.0";

  /**
   * Publish an Alert.
   * 
   * @param alert
   *          The alert to publish.
   */
  public void publishAlert(Alert alert) {
    System.out.println("--- ALERT");

    logger.log(Level.INFO, "publishAlert");
    final IHeader headerObj = new com.test.domain.Header(protocolVersion, applicationName, AppConfig.COMPONENT_TYPE);
    rabbitTemplate.convertAndSend(AppConfig.EXCHANGE_ALERT, "/", alert, m -> {
      // Insert header
      m.getMessageProperties().getHeaders().putAll(headerObj.toMap());
      return m;
    });

    System.out.println("--- ALERT 2");
  }
}
