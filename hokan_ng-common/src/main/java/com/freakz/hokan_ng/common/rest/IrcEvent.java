package com.freakz.hokan_ng.common.rest;

import com.freakz.hokan_ng.common.rest.messages.router.RestMessageData;

import java.io.Serializable;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 1:30 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class IrcEvent extends RestMessageData implements Serializable {

  private static final long serialVersionUID = 1L;

  private String botNick;

  private String network;
  private String channel;
  private String sender;
  private String login;
  private String hostname;
  private String mask;

  public IrcEvent() {
    super();
  }

  public IrcEvent(String botNick, String network, String channel, String sender, String login, String hostname) {
    super();
    this.botNick = botNick;
    this.network = network;
    this.channel = channel;
    this.sender = sender;
    this.login = login;
    this.hostname = hostname;
    this.mask = sender + "!" + login + "@" + hostname;
  }

  public IrcEvent(IrcEvent ircEvent) {
    this.botNick = ircEvent.botNick;
    this.network = ircEvent.network;
    this.channel = ircEvent.channel;
    this.sender = ircEvent.sender;
    this.login = ircEvent.login;
    this.hostname = ircEvent.hostname;
    this.mask = ircEvent.mask;
  }

  public String getBotNick() {
    return this.botNick;
  }

  public void setBotNick(String nick) {
    this.botNick = nick;
  }

  public String getNetwork() {
    return network;
  }

  public void setNetwork(String network) {
    this.network = network;
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

  public String getMask() {
    return this.mask;
  }
}
