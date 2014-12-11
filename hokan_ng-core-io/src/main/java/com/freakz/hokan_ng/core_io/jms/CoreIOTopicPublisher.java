package com.freakz.hokan_ng.core_io.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by petria on 10.12.2014.
 */
@Slf4j
@Component
public class CoreIOTopicPublisher implements com.freakz.hokan_ng.common.jms.TopicPublisher {

  private JmsTemplate jmsTemplate;
  private Topic topic;

  public void produce(final Serializable object) {
    this.jmsTemplate.send(this.topic, new MessageCreator() {
      public Message createMessage(Session session) throws JMSException {
        ObjectMessage mm = session.createObjectMessage();
        mm.setObject(object);
        return mm;
      }
    });
  }

  @Required
  public void setConnectionFactory(ConnectionFactory connectionFactory) {
    this.jmsTemplate = new JmsTemplate(connectionFactory);
  }

  @Required
  public void setTopic(Topic topic) {
    this.topic = topic;
  }

}