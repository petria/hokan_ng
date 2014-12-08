package com.freakz.hokan_ng.core_engine.quetest;

import com.freakz.hokan_ng.common.rest.messages.router.RestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * Created by petria on 8.12.2014.
 */
public class JMSProducer {

  @Autowired
  private JmsTemplate jmsTemplate;

  public void setJmsTemplate(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  public JmsTemplate getJmsTemplate() {
    return this.jmsTemplate;
  }


  public void sendMessages(final String messageText) throws JMSException {

    jmsTemplate.send(new MessageCreator() {

      public Message createMessage(Session session) throws JMSException {

        TextMessage message = session.createTextMessage("test message");
        RestMessage test = new RestMessage();

        message.setStringProperty("text", messageText);
        message.setObjectProperty("test", test);

        return message;

      }

    });

  }

  public String receiveMessages() throws JMSException {
    String msg = jmsTemplate.receive().getStringProperty("text");
    System.out.println("Getting message from queue " + msg);
    return msg;
  }

}
