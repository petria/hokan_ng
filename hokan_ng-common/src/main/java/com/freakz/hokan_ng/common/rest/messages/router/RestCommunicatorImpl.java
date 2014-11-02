package com.freakz.hokan_ng.common.rest.messages.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created on 3.9.2014.
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("singleton")
@Slf4j
public class RestCommunicatorImpl implements RestCommunicator {

  @Autowired
  private ApplicationContext context;


  @Override
  public void sendRestMessage(RestMessage message, RestResponseHandler responseHandler) {

    AsyncRestMessageSender sender = context.getBean(AsyncRestMessageSender.class);
    sender.sendRestMessage(message, responseHandler);

  }

}
