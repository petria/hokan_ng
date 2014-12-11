package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.rest.messages.router.RestMessage;
import com.freakz.hokan_ng.core_engine.jms.CoreEngineTopicPublisher;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by petria on 10.12.2014.
 */
@Component
@Slf4j
@Scope("prototype")
public class JMSSendCmd extends Cmd {


  @Autowired
  private CoreEngineTopicPublisher engineTopicPublisher;

  public JMSSendCmd() {
    super();
    setHelp("test.");
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
//    testProducer.produce(request.getIrcEvent().getMessage(), "bar");
    RestMessage restMessage = new RestMessage();
    restMessage.setMessageData("message", request.getIrcEvent().getMessage());
    engineTopicPublisher.publish(restMessage, ".*");
    response.addResponse("Sent!");
  }
}