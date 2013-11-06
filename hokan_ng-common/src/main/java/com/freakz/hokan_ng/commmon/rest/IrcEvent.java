package com.freakz.hokan_ng.commmon.rest;

import java.io.Serializable;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 1:30 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class IrcEvent implements Serializable {

  private static final long serialVersionUID = 1L;

  private String channel;
  private String sender;
  private String login;
  private String hostname;
  private String message;

  public IrcEvent() {
  }

  public IrcEvent(String channel, String sender, String login, String hostname, String message) {
    this.channel = channel;
    this.sender = sender;
    this.login = login;
    this.hostname = hostname;
    this.message = message;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static IrcEvent create(String channel, String sender, String login, String hostname, String message) {
    return new IrcEvent(channel, sender, login, hostname, message);
  }
}
