package com.freakz.hokan_ng.common.rest;

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
  private IrcMessageEvent ircEvent;
  private String engineAddress;
  private String outputPrefix;
  private String outputPostfix;

  private long myPid;

  public EngineRequest() {
  }

  public EngineRequest(IrcMessageEvent ircEvent) {
    this.ircEvent = ircEvent;
  }

  public IrcMessageEvent getIrcEvent() {
    return ircEvent;
  }

  public void setIrcEvent(IrcMessageEvent ircEvent) {
    this.ircEvent = ircEvent;
  }

  public long getMyPid() {
    return myPid;
  }

  public void setMyPid(long myPid) {
    this.myPid = myPid;
  }

  public String getEngineAddress() {
    return engineAddress;
  }

  public void setEngineAddress(String engineAddress) {
    this.engineAddress = engineAddress;
  }

  public String getOutputPrefix() {
    return outputPrefix;
  }

  public void setOutputPrefix(String outputPrefix) {
    this.outputPrefix = outputPrefix;
  }

  public String getOutputPostfix() {
    return outputPostfix;
  }

  public void setOutputPostfix(String outputPostfix) {
    this.outputPostfix = outputPostfix;
  }
}
