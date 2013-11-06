package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.commmon.rest.EngineRequest;
import com.freakz.hokan_ng.commmon.rest.EngineResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

  public EngineCommunicatorImpl() {
  }

  public HttpHeaders getRestHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("username", "s0me0ne");
    return httpHeaders;
  }

  public String getRestBaseUrl() {
    return "http://localhost:8080/hokan_ng-core-engine/";
  }

  @Override
  public EngineResponse sendEngineMessage(String request) {
    RestTemplate restTemplate = new RestTemplate();
    EngineRequest engineRequest = new EngineRequest(request);
    HttpEntity<EngineRequest> httpEntity = new HttpEntity<>(engineRequest, getRestHeaders());
    String restUrl = getRestBaseUrl() + "handle";
    log.info("Making request to: " + restUrl);
    ResponseEntity<EngineResponse> responseEnt
        = restTemplate.exchange(restUrl, HttpMethod.POST, httpEntity, EngineResponse.class);

    EngineResponse response = responseEnt.getBody();
    return response;

/*    HttpEntity<String> httpEntity = new HttpEntity<>(request, getRestHeaders());
    String restUrl = getRestBaseUrl() + "/test";
    ResponseEntity<String> responseEnt
        = restTemplate.exchange(restUrl, HttpMethod.GET, httpEntity, String.class);

    String response = responseEnt.getBody();
    return new EngineResponse(response);*/

  }

}
