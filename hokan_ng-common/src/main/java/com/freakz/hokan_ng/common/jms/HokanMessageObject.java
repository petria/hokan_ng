package com.freakz.hokan_ng.common.jms;

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
}
