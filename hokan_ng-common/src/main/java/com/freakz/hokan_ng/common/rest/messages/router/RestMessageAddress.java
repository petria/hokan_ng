package com.freakz.hokan_ng.common.rest.messages.router;

import com.freakz.hokan_ng.common.entity.RestUrlType;

import java.io.Serializable;

/**
 * Created on 4.9.2014.
 *
 * @author Petri Airio <petri.j.airio@>
 */
public class RestMessageAddress implements Serializable {

  private RestUrlType restUrlType;

  private int instanceKey;

  public RestMessageAddress() {
  }

  public RestMessageAddress(RestUrlType restUrlType, int instanceKey) {
    this.restUrlType = restUrlType;
    this.instanceKey = instanceKey;
  }

  public RestUrlType getRestUrlType() {
    return restUrlType;
  }

  public void setRestUrlType(RestUrlType restUrlType) {
    this.restUrlType = restUrlType;
  }

  public int getInstanceKey() {
    return instanceKey;
  }

  public void setInstanceKey(int instanceKey) {
    this.instanceKey = instanceKey;
  }
}
