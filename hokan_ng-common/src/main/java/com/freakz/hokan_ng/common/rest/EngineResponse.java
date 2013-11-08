package com.freakz.hokan_ng.common.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 10:42 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class EngineResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private int responseStatus;
  private String responseMessage;
  private EngineRequest request;

  private List<EngineMethodCall> engineMethodCalls = new ArrayList<>();

  public EngineResponse() {
  }

  public EngineResponse(EngineRequest request) {
    this.request = request;
  }

  public int getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(int responseStatus) {
    this.responseStatus = responseStatus;
  }

  public String getResponseMessage() {
    return responseMessage;
  }

  public void setResponseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
  }

  public EngineRequest getRequest() {
    return request;
  }

  public void addEngineMethodCall(String methodName, String... methodArgs) {
    this.engineMethodCalls.add(new EngineMethodCall(methodName, methodArgs));
  }

  public List<EngineMethodCall> getEngineMethodCalls() {
    return engineMethodCalls;
  }

  public void setEngineMethodCalls(List<EngineMethodCall> engineMethodCalls) {
    this.engineMethodCalls = engineMethodCalls;
  }
}
