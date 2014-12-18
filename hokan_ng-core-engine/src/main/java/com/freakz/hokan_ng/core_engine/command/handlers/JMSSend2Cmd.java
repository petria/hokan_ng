package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.jms.HokanJMSMessageHandler;
import com.freakz.hokan_ng.common.jms.messages.HokanMessageObject;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Component
@Slf4j
@Scope("prototype")
public class JMSSend2Cmd extends Cmd {

  @Autowired
  private HokanJMSMessageHandler messageHandler;

  public JMSSend2Cmd() {
    super();
    setHelp("test.");
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    log.info("Doing query...");
    HokanMessageObject messageObject = messageHandler.createQuery(topicListener, topicPublisher);
    log.info("Query result: {}", messageObject);

    response.addResponse("Got reply %s", messageObject + "");
  }
}