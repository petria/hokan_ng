package com.freakz.hokan_ng.common.rest.messages;


import com.freakz.hokan_ng.common.entity.Alias;
import com.freakz.hokan_ng.common.entity.RestUrl;
import com.freakz.hokan_ng.common.entity.RestUrlType;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.service.AliasService;
import com.freakz.hokan_ng.common.service.RestUrlService;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
@Scope("singleton")
@Slf4j
public class EngineCommunicatorImpl implements EngineCommunicator, ResponseErrorHandler {

  @Autowired
  private ApplicationContext context;

  @Autowired
  private AliasService aliasService;

  @Autowired
  private RestUrlService restUrlService;
  private int counter = 0;

  private Map<String, RestUrl> engineHandlers = new HashMap<>();

  private Map<String, RestUrl> engineHandlersCycle = new HashMap<>();


  public EngineCommunicatorImpl() {
  }

  @Scheduled(fixedDelay = 1000)
  private void checkEngines() {
    String instanceKey = "1234"; // TODO engineProperties.getProperty("INSTANCE_KEY");
    counter++;
    if (engineHandlers.size() == 0) {
      doCheckEngines(instanceKey);
    } else {
      if (counter % 10 == 0) {
        doCheckEngines(instanceKey);
      }
    }
  }

  private void doCheckEngines(String instanceKey) {
    List<RestUrl> restUrls = restUrlService.getRestUrls(instanceKey, RestUrlType.CORE_ENGINE);
    for (RestUrl restUrl : restUrls) {

/*      if (this.engineHandlers.containsKey(restUrl.getRestUrl())) {
        continue;
      }*/
//      log.info("Checking validity of: {}", restUrl.toString());
      String url = restUrl.getRestUrl() + "/ping";
      RestTemplate restTemplate = new RestTemplate() {
      };
      restTemplate.setErrorHandler(this);

      String response;
      try {
        ResponseEntity<String> responseEnt
            = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        response = responseEnt.getBody();
        if (response.equals("pong")) {
//          log.info("Got response from {}", url);

          addToCycleList(restUrl);
        }

      } catch (Exception e) {
        removeFromCycleList(restUrl);
      }

    }

  }

  private synchronized void addToCycleList(RestUrl restUrl) {
    this.engineHandlers.put(restUrl.getRestUrl(), restUrl);
    this.engineHandlersCycle.put(restUrl.getRestUrl(), restUrl);
  }

  private synchronized void removeFromCycleList(RestUrl restUrl) {
    this.engineHandlers.remove(restUrl.getRestUrl());
    this.engineHandlersCycle.remove(restUrl.getRestUrl());
  }

  private boolean resolveAlias(EngineRequest request) {
    String line = request.getIrcEvent().getMessage();
    List<Alias> aliases = aliasService.findAliases();
    for (Alias alias : aliases) {
      if (line.equals(alias.getAlias())) {
        request.getIrcEvent().setMessage(alias.getCommand());
        return true;
      }
    }
    return false;
  }


  @Override
  public void sendEngineMessage(EngineRequest request, EngineEventHandler engineEventHandler) {

    boolean aliased = resolveAlias(request);
    String message = request.getIrcEvent().getMessage();

    boolean between = StringStuff.isInBetween(message, "&&", ' ');
    log.info("between = {}", between);
    if (!message.startsWith("!alias") && between) {
      String[] split = message.split("\\&\\&");
      for (String splitted : split) {
        EngineRequest splitRequest = new EngineRequest((IrcMessageEvent) request.getIrcEvent().clone());
        String trimmed = splitted.trim();
        splitRequest.setOutputPrefix(trimmed + " :: ");
        splitRequest.getIrcEvent().setMessage(trimmed);
        doSendRequest(splitRequest, engineEventHandler);
//        doSendRequestCycle(splitRequest, engineEventHandler);
      }
    } else {
      if (aliased) {
        request.setOutputPrefix(request.getIrcEvent().getMessage() + " :: ");
      }
      doSendRequest(request, engineEventHandler);
//      doSendRequestCycle(request, engineEventHandler);
    }
  }

  private synchronized void doSendRequest(EngineRequest request, EngineEventHandler engineEventHandler) {

    if (engineHandlers.size() == 0) {
      log.warn("No registered engines!!!");
      return;
    }

    for (RestUrl restUrl : engineHandlers.values()) {
      AsyncEngineMessageSender sender = context.getBean(AsyncEngineMessageSender.class);
      request.setEngineAddress(restUrl.getRestUrl());
      sender.sendRequest(request, engineEventHandler);
    }

  }

  private synchronized void doSendRequestCycle(EngineRequest request, EngineEventHandler engineEventHandler) {

    if (engineHandlersCycle.size() == 0) {
      log.warn("No registered engines!!!");
      return;
    }

    RestUrl restUrl;
    boolean reCycle = false;
    if (engineHandlersCycle.size() > 1) {
      restUrl = engineHandlersCycle.remove(0);
      reCycle = true;
    } else {
      restUrl = engineHandlersCycle.get(0);
    }

    AsyncEngineMessageSender sender = context.getBean(AsyncEngineMessageSender.class);
    request.setEngineAddress(restUrl.getRestUrl());
    sender.sendRequest(request, engineEventHandler);

    if (reCycle) {
      //engineHandlersCycle.add(restUrl); TODO fix
    }

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

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    return false;
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    log.info("errororororor!");
  }

  @Override
  public Map<String, RestUrl> getEngineHandlers() {
    return this.engineHandlers;
  }
}
