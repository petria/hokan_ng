package com.freakz.hokan_ng.common.engine;


import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.util.StringStuff;
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
    String message = request.getIrcEvent().getMessage();
    boolean between = StringStuff.isInBetween(message, "&&", ' ');
    log.info("between = {}", between);
    if (between) {
      String[] split = message.split("\\&\\&");
      for (String splitted : split) {
        EngineRequest splitRequest = new EngineRequest((IrcMessageEvent) request.getIrcEvent().clone());
        String trimmed = splitted.trim();
        splitRequest.getIrcEvent().setMessage(trimmed);
        sender.sendRequest(splitRequest, engineEventHandler);
      }
    } else {
      sender.sendRequest(request, engineEventHandler);
    }
  }

}
