package com.freakz.hokan_ng.common.rest.messages.router;

import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
import com.freakz.hokan_ng.common.exception.HokanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created on 4.9.2014.
 *
 * @author Petri Airio <petri.j.airio@>
 */
@Slf4j
@Component
@Scope("prototype")
public class AsyncRestMessageSender implements CommandRunnable {

  private CommandPool commandPool;
  private RestResponseHandler responseHandler;

  @Autowired
  public void setCommandPool(CommandPool commandPool) {
    this.commandPool = commandPool;
  }

  public void sendRestMessage(RestMessage message, RestResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
    this.commandPool.startRunnable(this, message);
  }

  public HttpHeaders getRestHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("username", "s0me0ne");
    return httpHeaders;
  }


  @Override
  public void handleRun(long myPid, Object args) throws HokanException {
    RestMessage message = (RestMessage) args;
    RestMessageAddress address = message.getMessageAddress();

    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<RestMessage> httpEntity = new HttpEntity<>(message, getRestHeaders());

    String restUrl = "http://localhost:8080/hokanng-core-engine/handleRestMessage";

    log.info("\nMaking request to: " + restUrl);

    try {

      RestMessage response;
      ResponseEntity<RestMessage> responseEnt
          = restTemplate.exchange(restUrl, HttpMethod.POST, httpEntity, RestMessage.class);


      response = responseEnt.getBody();
      this.responseHandler.handleRestResponse(response);

    } catch (Exception e) {
      int foo = 0;
    }

  }


}
