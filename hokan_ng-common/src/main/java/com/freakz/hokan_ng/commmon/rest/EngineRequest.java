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
  private IrcEvent ircEvent;
  private long myPid;

  public EngineRequest() {
  }

  public EngineRequest(IrcEvent ircEvent) {
    this.ircEvent = ircEvent;
  }

  public IrcEvent getIrcEvent() {
    return ircEvent;
  }

  public void setIrcEvent(IrcEvent ircEvent) {
    this.ircEvent = ircEvent;
  }

  public long getMyPid() {
    return myPid;
  }

  public void setMyPid(long myPid) {
    this.myPid = myPid;
  }
}
