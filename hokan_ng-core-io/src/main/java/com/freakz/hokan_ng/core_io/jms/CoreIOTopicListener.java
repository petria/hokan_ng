package com.freakz.hokan_ng.core_io.jms;

import com.freakz.hokan_ng.common.jms.HokanTopicFollower;
import com.freakz.hokan_ng.common.jms.HokanTopicListenerBase;
import com.freakz.hokan_ng.common.jms.HokanTopicTypes;
import com.freakz.hokan_ng.common.jms.messages.HokanMessageObject;
import com.freakz.hokan_ng.common.rest.messages.EngineEventHandler;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreIOTopicListener extends HokanTopicListenerBase implements HokanTopicFollower {

  private EngineEventHandler engineEventHandler;


  public void setEngineEventHandler(EngineEventHandler engineEventHandler) {
    this.engineEventHandler = engineEventHandler;
  }

  @Override
  public String getAcceptedJMSType() {
    return HokanTopicTypes.TO_IO;
  }

  @Override
  public void onTopicMessage(HokanMessageObject message) {
    EngineResponse response = (EngineResponse) message.getData("engineResponse");
    if (response != null) {
      log.info("message!: {}", message);
      engineEventHandler.handleEngineResponse(response);
    }
  }

}

