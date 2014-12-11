package com.freakz.hokan_ng.core_engine.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import com.freakz.hokan_ng.common.jms.HokanTopicPublisher;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreEngineTopicPublisher implements HokanTopicPublisher {

  private JmsTemplate jmsTemplate;
  private Topic topic;

  public void publish(final Serializable object, final String jmsType) {
    this.jmsTemplate.send(this.topic, new MessageCreator() {
      public Message createMessage(Session session) throws JMSException {
        ObjectMessage mm = session.createObjectMessage();
        mm.setJMSType(jmsType);
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