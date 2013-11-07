package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.IrcEvent;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 4:02 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class CommandBase {

  public String getName() {
    return "Name";
  }

  public String getMatchPattern() {
    return "";
  }

  public String handleLine(IrcEvent ircEvent) {
    return "fdfsd";
  }

}
