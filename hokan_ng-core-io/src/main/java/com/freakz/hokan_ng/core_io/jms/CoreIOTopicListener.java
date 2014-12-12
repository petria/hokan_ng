package com.freakz.hokan_ng.core_io.jms;

import com.freakz.hokan_ng.common.jms.HokanMessageObject;
import com.freakz.hokan_ng.common.jms.HokanTopicFollower;
import com.freakz.hokan_ng.common.jms.HokanTopicListenerImpl;
import com.freakz.hokan_ng.common.rest.messages.EngineEventHandler;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreIOTopicListener extends HokanTopicListenerImpl implements HokanTopicFollower {

  private EngineEventHandler engineEventHandler;

  public CoreIOTopicListener() {
    followTopic(this);
  }

  public void setEngineEventHandler(EngineEventHandler engineEventHandler) {
    this.engineEventHandler = engineEventHandler;
  }

  @Override
  public void onMessage(HokanMessageObject message) {
    engineEventHandler.handleEngineResponse((EngineResponse) message.getData("engineResponse"));
  }

}

