package com.freakz.hokan_ng.common.jms.messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by petria on 11.12.2014.
 */
public class HokanMessageObject implements Serializable {

  private Map<String, Object> payload = new HashMap<>();

  public void setData(String key, Object obj) {
    payload.put(key, obj);
  }

  public Object getData(String key) {
    return payload.get(key);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass());
    sb.append(" payLoad: ");
    for (String key : payload.keySet()) {
      Object value = payload.get(key);
      sb.append("key: ");
      sb.append(key);
      sb.append("=");
      sb.append(value);
      sb.append("  ");
    }
    return sb.toString();
  }
}
