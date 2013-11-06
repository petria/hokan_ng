package com.freakz.hokan_ng.commmon.rest;

import java.io.Serializable;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 10:42 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class EngineRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String request;

  public EngineRequest() {
  }

  public EngineRequest(String request) {
    this.request = request;
  }

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

}
