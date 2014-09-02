package com.freakz.hokan_ng.common.rest.messages;

import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
import com.freakz.hokan_ng.common.rest.CoreRequest;
import com.freakz.hokan_ng.common.rest.CoreResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * User: petria
 * Date: 12/12/13
 * Time: 2:19 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
@Component
@Scope("prototype")
public class AsyncCoreMessageSender implements CommandRunnable {

  private CommandPool commandPool;

  private CoreEventHandler coreEventHandler;

  public AsyncCoreMessageSender() {
  }

  @Autowired
  public void setCommandPool(CommandPool commandPool) {
    this.commandPool = commandPool;
  }

  public void sendRequest(CoreRequest request, CoreEventHandler coreEventHandler) {
    this.coreEventHandler = coreEventHandler;
    commandPool.startRunnable(this, request);
  }

  public String getRestBaseUrl() {
//    return "http://localhost:9080/hokan_ng-core-engine/";
//    return "http://localhost:8080//hokanng-core-io";
    return "http://localhost:8080/hokan_ng-core-io-0.0.1-SNAPSHOT/";
  }

  public HttpHeaders getRestHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("username", "s0me0ne");
    return httpHeaders;
  }

  @Override
  public void handleRun(long myPid, Object args) {
    CoreRequest request = (CoreRequest) args;
//    request.setMyPid(myPid);

    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<CoreRequest> httpEntity = new HttpEntity<>(request, getRestHeaders());
    String restUrl = getRestBaseUrl() + "/control/handle";
    log.info("\nMaking request to: " + restUrl);
    CoreResponse response;
    try {
      ResponseEntity<CoreResponse> responseEnt
          = restTemplate.exchange(restUrl, HttpMethod.POST, httpEntity, CoreResponse.class);
      response = responseEnt.getBody();
      this.coreEventHandler.handleCoreResponse(response);

    } catch (HttpClientErrorException e) {
/*      HokanEngineException engineException = new HokanEngineException(e, getRestBaseUrl());
      response = new EngineResponse(request);
      response.setException(engineException.toString());
      response.setResponseStatus(-100);
      response.setResponseMessage(e.getClass() + " -> " + e.getMessage());
      this.engineEventHandler.handleEngineError(response);*/
    }
  }

}
