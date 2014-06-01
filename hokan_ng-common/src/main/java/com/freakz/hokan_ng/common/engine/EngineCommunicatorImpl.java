package com.freakz.hokan_ng.common.engine;


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
          this.engineHandlers.put(restUrl.getRestUrl(), restUrl);
        }

      } catch (Exception e) {
        this.engineHandlers.remove(restUrl.getRestUrl());
      }

    }

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
      log.warn("No registered engines!!!");
      return;
    }

    for (RestUrl restUrl : engineHandlers.values()) {
      AsyncEngineMessageSender sender = context.getBean(AsyncEngineMessageSender.class);
      request.setEngineAddress(restUrl.getRestUrl());
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

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    return false;
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    log.info("errororororor!");
  }
}
