package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.commmon.rest.EngineRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 10:50 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class EngineCommunicatorImpl implements EngineCommunicator {

  @Autowired
  private ApplicationContext context;

  public EngineCommunicatorImpl() {
  }

  @Override
  public void sendEngineMessage(EngineRequest request, EngineEventHandler engineEventHandler) {

    AsyncEngineMessageSender sender = context.getBean(AsyncEngineMessageSender.class);
    sender.sendRequest(request, engineEventHandler);
  }

}
