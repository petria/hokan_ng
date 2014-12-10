package com.freakz.hokan_ng.core_services.jms;

import lombok.extern.slf4j.Slf4j;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreServicesTopicListener implements MessageListener {


  @Override
  public void onMessage(Message message) {
    try {
      if (message instanceof ObjectMessage) {
        log.info("ObjectMessage: {}", message);
        ObjectMessage mapMessage = (ObjectMessage) message;
        Object obj = mapMessage.getObject();
        //... do something with the data here
      }
    } catch (Exception e) {
      log.error("Error: {}", e);
    }
  }
}
