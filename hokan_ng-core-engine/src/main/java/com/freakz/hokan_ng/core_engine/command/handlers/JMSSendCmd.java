package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.core_engine.jms.TestProducer;
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
  private TestProducer testProducer;

  public JMSSendCmd() {
    super();
    setHelp("test.");
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    testProducer.produce(request.getIrcEvent().getMessage(), "bar");
    response.addResponse("Sent!");
  }
}