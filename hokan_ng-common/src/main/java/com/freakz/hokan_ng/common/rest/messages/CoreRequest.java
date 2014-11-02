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
public class CoreRequest extends RestMessageData implements Serializable {

  private static final long serialVersionUID = 1L;

  private long targetChannelId;
  private String message;

  public CoreRequest() {
  }

  public CoreRequest(long targetChannelId, String message) {
    this.targetChannelId = targetChannelId;
    this.message = message;
  }

  public long getTargetChannelId() {
    return targetChannelId;
  }

  public void setTargetChannelId(long targetChannelId) {
    this.targetChannelId = targetChannelId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
