package com.freakz.hokan_ng.common.rest.messages;

import com.freakz.hokan_ng.common.rest.messages.router.RestMessageData;

import java.io.Serializable;

/**
 * User: petria
 * Date: 12/12/13
 * Time: 2:14 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class CoreResponse extends RestMessageData implements Serializable {

  private static final long serialVersionUID = 1L;

  private CoreRequest request;

  public CoreResponse() {
  }

  public CoreRequest getRequest() {
    return request;
  }

  public void setRequest(CoreRequest request) {
    this.request = request;
  }

}
