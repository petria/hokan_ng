package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.core_engine.quetest.JMSProducer;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 *
 * Created by petria on 8.12.2014.
 */
@Component
@Slf4j
@Scope("prototype")
public class Hornet1Cmd extends Cmd {

  @Autowired
  private JMSProducer jms;

  public Hornet1Cmd() {
    super();
    setHelp("test.");
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
      log.info("-- Receiving message from: " + jms);
      try {
        String receive = jms.receiveMessages();
        response.addResponse("Got msg: %s", receive);
      } catch (JMSException e) {
        e.printStackTrace();
      }
  }
}
