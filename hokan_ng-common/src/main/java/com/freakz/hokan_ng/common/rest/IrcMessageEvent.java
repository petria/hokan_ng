package com.freakz.hokan_ng.common.rest;

import java.io.Serializable;

/**
 * User: petria
 * Date: 11/15/13
 * Time: 12:34 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class IrcMessageEvent extends IrcEvent implements Serializable {

  private static final long serialVersionUID = 1L;

  private String message;
  private boolean isPrivate;
  private boolean isToMe;

  public IrcMessageEvent() {
    super();
  }

  public IrcMessageEvent(String channel, String sender, String login, String hostname, String message) {
    super(channel, sender, login, hostname);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isToMe() {
    return isToMe;
  }

  public void setToMe(boolean toMe) {
    isToMe = toMe;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }
}
