package com.freakz.hokan_ng.core_engine.quetest;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 *
 * Created by petria on 9.12.2014.
 */
@Slf4j
@Component
public class TestProducer {

  private JmsTemplate jmsTemplate;
  private Queue queue;

  public void produce(final String foo, final String bar) {
    this.jmsTemplate.send(this.queue, new MessageCreator() {
      public Message createMessage(Session session) throws JMSException {
        MapMessage mm = session.createMapMessage();
        mm.setString("foo", foo);
        mm.setString("bar", bar);
        return mm;
      }
    });
    log.info("Message sent to message broker");
  }

  @Required
  public void setConnectionFactory(ConnectionFactory connectionFactory) {
    this.jmsTemplate = new JmsTemplate(connectionFactory);
  }

  @Required
  public void setQueue(Queue queue) {
    this.queue = queue;
  }
}
