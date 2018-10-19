package com.test;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.util.ErrorHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.domain.EventAlert;
import com.test.domain.EventKpisCollected;

@Configuration
public class AppConfig {
  public static final String EXCHANGE_KPIS = new EventKpisCollected().getEventId();
  public static final String EXCHANGE_ALERT = new EventAlert().getEventId();
  public static final String COMPONENT_TYPE = "MY_COMPONENT_1";
  
  private static final boolean E_DURABLE = true;
  private static final boolean E_AUTO_DELETE = false;

  private static final boolean Q_DURABLE = false;
  private static final boolean Q_EXCLUSIVE = true;
  private static final boolean Q_AUTO_DELETE = true;
 

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  public Helper helper() {
    return new Helper();
  }
  
  @Bean
  public ErrorHandler errorHandler() {
    return new BrokerExceptionHandler();
  }

  @Bean
  public MessageConverter jsonConverter() {
    Gson gson = new GsonBuilder().create();
    return new MessageConverter() {

      @Override
      public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        System.out.println("toMessage");
        return new Message(gson.toJson(object).getBytes(), messageProperties);
      }

      @Override
      public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("fromMessage");
        message.getMessageProperties().setContentType("application/json");
        throw new UnsupportedOperationException();
      }
    };
  }

  @Bean
  @Qualifier("queueKpis")
  Queue queueKpis() {
    return new Queue(getQueueKpis(), Q_DURABLE, Q_EXCLUSIVE, Q_AUTO_DELETE);
  }

  @Bean
  @Qualifier("queueAlert")
  Queue queueAlert() {
    return new Queue(getQueueAlert(), Q_DURABLE, Q_EXCLUSIVE, Q_AUTO_DELETE);
  }

  @Bean
  @Qualifier("exchangeKpis")
  @Primary
  FanoutExchange exchangeKpis() {
    return new FanoutExchange(AppConfig.EXCHANGE_KPIS, E_DURABLE, E_AUTO_DELETE);
  }

  @Bean
  @Qualifier("exchangeAlert")
  FanoutExchange exchangeAlert() {
    return new FanoutExchange(AppConfig.EXCHANGE_ALERT, E_DURABLE, E_AUTO_DELETE);
  }

  @Bean
  Binding bindingKpis(@Qualifier("queueKpis") Queue queue, @Qualifier("exchangeKpis") FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
  }

  @Bean
  Binding bindingReset(@Qualifier("queueAlert") Queue queue, @Qualifier("exchangeAlert") FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
  }

  @Bean
  SimpleMessageListenerContainer containerKpis(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerKpisAdapter) {

    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setDefaultRequeueRejected(false);
    container.setErrorHandler(errorHandler());
    container.setMessageConverter(jsonConverter());
    container.setQueueNames(getQueueKpis());
    container.setMessageListener(listenerKpisAdapter);
    return container;
  }

  @Bean
  SimpleMessageListenerContainer containerReset(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAlertAdapter) {

    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setDefaultRequeueRejected(false);
    container.setErrorHandler(errorHandler());
    container.setMessageConverter(jsonConverter());
    container.setQueueNames(getQueueAlert());
    container.setMessageListener(listenerAlertAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerKpisAdapter(Application receiver) {
    return new MessageListenerAdapter(receiver, "kpisEvent");
  }

  @Bean
  MessageListenerAdapter listenerAlertAdapter(Application receiver) {
    return new MessageListenerAdapter(receiver, "alertEvent");
  }

  private String getQueueKpis() {
    return applicationName + "." + new EventKpisCollected().getEventId();
  }

  private String getQueueAlert() {
    return applicationName + "." + new EventAlert().getEventId();
  }

}