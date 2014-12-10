package com.freakz.hokan_ng.core.http.jms;

/**
 *
 * Created by petria on 10.12.2014.
 */

import lombok.extern.slf4j.Slf4j;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Slf4j
//@Component
public class CoreListener implements MessageListener {

  @Override
  public void onMessage(Message message) {
    log.info("Got message: {}", message);
    try {
      if (message instanceof MapMessage) {
        MapMessage mapMessage = (MapMessage) message;
        String foo = mapMessage.getString("foo");
        String bar = mapMessage.getString("bar");
        //...do something with the data you just got
      }
    } catch (Exception e) {
      log.error("Error: {}", e);
    }
  }
}