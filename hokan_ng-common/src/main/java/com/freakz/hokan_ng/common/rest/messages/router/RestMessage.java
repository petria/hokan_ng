package com.freakz.hokan_ng.common.rest.messages.router;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 3.9.2014.
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class RestMessage implements Serializable {

  private RestMessageAddress messageAddress;
  public Object test;
  private final Map<Object, Object> payload = new HashMap<>();

  public RestMessage() {
  }

  public RestMessage(final RestMessageAddress messageAddress) {
    this.messageAddress = messageAddress;
  }

  public RestMessageAddress getMessageAddress() {
    return messageAddress;
  }

  public Map<Object, Object> getPayload() {
    return payload;
  }

  public Object getMessageData(Object type) {
    return payload.get(type);
  }

  public void setMessageData(Object type, Object data) {
    payload.put(type, data);
  }

}
