package com.freakz.hokan_ng.common.engine;

import com.freakz.hokan_ng.common.exception.HokanEngineException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
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
 * Date: 11/6/13
 * Time: 1:25 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
@Component
@Scope("prototype")
public class AsyncEngineMessageSender implements CommandRunnable {

  private CommandPool commandPool;

  private EngineEventHandler engineEventHandler;

  public AsyncEngineMessageSender() {
  }

  @Autowired
  public void setCommandPool(CommandPool commandPool) {
    this.commandPool = commandPool;
  }

  public void sendRequest(EngineRequest request, EngineEventHandler engineEventHandler) {
    this.engineEventHandler = engineEventHandler;
    commandPool.startRunnable(this, request);
  }


  public HttpHeaders getRestHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("username", "s0me0ne");
    return httpHeaders;
  }


  @Override
  public void handleRun(long myPid, Object args) {
    EngineRequest request = (EngineRequest) args;
    request.setMyPid(myPid);

    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<EngineRequest> httpEntity = new HttpEntity<>(request, getRestHeaders());
//    String restUrl = getRestBaseUrl() + "handle";
    String restUrl = request.getEngineAddress() + "handle";
    log.info("\nMaking request to: " + restUrl);
    EngineResponse response;
    try {
      ResponseEntity<EngineResponse> responseEnt
          = restTemplate.exchange(restUrl, HttpMethod.POST, httpEntity, EngineResponse.class);
      response = responseEnt.getBody();
      this.engineEventHandler.handleEngineResponse(response);

    } catch (HttpClientErrorException hcee) {


    } catch (Exception e) {
      HokanEngineException engineException = new HokanEngineException(e, restUrl);
      response = new EngineResponse(request);
      response.setException(engineException.toString());
      response.setResponseStatus(-100);
      response.setResponseMessage(e.getClass() + " -> " + e.getMessage());
      this.engineEventHandler.handleEngineError(response);
    }
  }

}
