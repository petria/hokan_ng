package com.freakz.hokan_ng.core_io.jms;

import com.freakz.hokan_ng.common.jms.HokanTopicListener;
import com.freakz.hokan_ng.common.jms.HokanTopicMessageObject;
import com.freakz.hokan_ng.common.jms.HokanTopicTypes;
import com.freakz.hokan_ng.common.rest.messages.EngineEventHandler;
import lombok.extern.slf4j.Slf4j;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreIOTopicListener implements MessageListener, HokanTopicListener {


  private EngineEventHandler engineEventHandler;

  @Override
  public void onMessage(Message message) {
//    log.info("Message: {}", message);
    try {
      if (message instanceof ObjectMessage) {
        if (message.getJMSType().equals(HokanTopicTypes.TO_IO)) {
          ObjectMessage mapMessage = (ObjectMessage) message;
          HokanTopicMessageObject messageObject = (HokanTopicMessageObject) mapMessage.getObject();
          this.engineEventHandler.handleEngineResponse((com.freakz.hokan_ng.common.rest.messages.EngineResponse) messageObject.getData("engineResponse"));
        }
      }
    } catch (Exception e) {
      log.error("Error: {}", e);
    }
  }

  public void setEngineEventHandler(EngineEventHandler engineEventHandler) {
    this.engineEventHandler = engineEventHandler;
  }
}
