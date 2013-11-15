package com.freakz.hokan_ng.common.rest;

import java.io.Serializable;

/**
 * User: petria
 * Date: 11/15/13
 * Time: 12:41 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class IrcPrivateMessageEvent extends IrcMessageEvent implements Serializable {

  private static final long serialVersionUID = 1L;

  public IrcPrivateMessageEvent(String sender, String login, String hostname, String message) {
    super(sender, sender, login, hostname, message);
  }

}
