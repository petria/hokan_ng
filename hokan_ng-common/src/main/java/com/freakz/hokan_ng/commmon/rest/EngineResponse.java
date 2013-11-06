package com.freakz.hokan_ng.commmon.rest;

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

  private String response;

  public EngineResponse() {
  }

  public EngineResponse(String response) {
    this.response = response;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }
}
