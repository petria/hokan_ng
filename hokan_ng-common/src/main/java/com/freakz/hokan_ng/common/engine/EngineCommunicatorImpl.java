package com.freakz.hokan_ng.common.engine;


import com.freakz.hokan_ng.common.entity.Alias;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.service.AliasService;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @Autowired
  private AliasService aliasService;


  private Map<String, String> engineHandlers = new HashMap<>();

  public EngineCommunicatorImpl() {
  }

  @Override
  public void sendEngineMessage(EngineRequest request, EngineEventHandler engineEventHandler) {

    String message = request.getIrcEvent().getMessage();
    message = resolveAlias(message);
    boolean between = StringStuff.isInBetween(message, "&&", ' ');
    log.info("between = {}", between);
    if (!message.startsWith("!alias") && between) {
      String[] split = message.split("\\&\\&");
      for (String splitted : split) {
        EngineRequest splitRequest = new EngineRequest((IrcMessageEvent) request.getIrcEvent().clone());
        String trimmed = splitted.trim();
        splitRequest.getIrcEvent().setMessage(trimmed);
        doSendRequest(splitRequest, engineEventHandler);
      }
    } else {
      doSendRequest(request, engineEventHandler);
    }
  }

  private void doSendRequest(EngineRequest request, EngineEventHandler engineEventHandler) {
    if (engineHandlers.size() == 0) {
      log.error("No registered engines!!!");
      return;
    }
    for (String engineAddress : engineHandlers.values()) {
      AsyncEngineMessageSender sender = context.getBean(AsyncEngineMessageSender.class);
      request.setEngineAddress(engineAddress);
      sender.sendRequest(request, engineEventHandler);

    }

  }

  @Override
  public void addEngineHandler(String engineId, String engineAddress) {

  }

  @Override
  public void removeEngineHandler(String engineId) {

  }

  @Override
  public void clearEngineHandlers() {

  }

  private String resolveAlias(String line) {
    List<Alias> aliases = aliasService.findAliases();
    for (Alias alias : aliases) {
      if (line.equals(alias.getAlias())) {
        return alias.getCommand();
      }
    }
    return line;
  }


}
