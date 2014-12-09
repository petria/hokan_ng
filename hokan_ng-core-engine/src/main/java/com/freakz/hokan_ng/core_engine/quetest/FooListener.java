package com.freakz.hokan_ng.core_engine.quetest;

/**
 *
 * Created by petria on 9.12.2014.
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Slf4j
@Component
public class FooListener implements MessageListener {

  @Override
  public void onMessage(Message message) {
    try {
      log.info("Message: {}", message);

      if (message instanceof MapMessage) {
        MapMessage mapMessage = (MapMessage) message;

        String foo = mapMessage.getString("foo");
        String bar = mapMessage.getString("bar");

      }
    } catch (Exception e) {
      log.error("error", e);
    }
  }
}