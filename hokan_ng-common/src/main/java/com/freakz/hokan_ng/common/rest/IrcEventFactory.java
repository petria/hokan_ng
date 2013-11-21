package com.freakz.hokan_ng.common.rest;

/**
 * User: petria
 * Date: 11/15/13
 * Time: 12:36 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class IrcEventFactory {

  public static IrcEvent createIrcEvent(String channel, String sender, String login, String hostname) {
    return new IrcEvent(channel, sender, login, hostname);
  }

  public static IrcEvent createIrcMessageEvent(String channel, String sender, String login, String hostname, String message) {
    return new IrcMessageEvent(channel, sender, login, hostname, message);
  }


}
