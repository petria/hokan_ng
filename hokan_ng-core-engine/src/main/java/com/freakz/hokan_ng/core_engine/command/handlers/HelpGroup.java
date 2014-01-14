package com.freakz.hokan_ng.core_engine.command.handlers;

/**
 * User: petria
 * Date: 12/12/13
 * Time: 9:49 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public enum HelpGroup {

  ALIAS("Alias help group"),
  CHANNELS("Channels help group"),
  NETWORK("Network help group"),
  PROCESS("Process help group"),
  PROPERTIES("Properties help group"),
  TV("TV help group"),
  UPDATERS("Updaters help group"),
  USERS("Users help group"),
  URLS("Urls help group");

  private String helpText;

  private HelpGroup(String helpText) {
    this.helpText = helpText;
  }

  public String getHelpText() {
    return this.helpText;
  }

}
