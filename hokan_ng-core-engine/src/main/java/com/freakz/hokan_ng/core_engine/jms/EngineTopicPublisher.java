package com.freakz.hokan_ng.core_engine.jms;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
@Component
public class EngineTopicPublisher {

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