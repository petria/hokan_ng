package com.freakz.hokan_ng.common.rest;

import java.io.Serializable;

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

  private String engineMethod;
  private String[] engineMethodArgs;


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

  public String getEngineMethod() {
    return engineMethod;
  }

  public void setEngineMethod(String engineMethod) {
    this.engineMethod = engineMethod;
  }

  public String[] getEngineMethodArgs() {
    return engineMethodArgs;
  }

  public void setEngineMethodArgs(String... engineMethodArgs) {
    this.engineMethodArgs = engineMethodArgs;
  }
}
