package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.service.AccessControlService;
import com.freakz.hokan_ng.common.util.CommandArgs;
import com.martiansoftware.jsap.IDMap;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Option;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 4:02 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public abstract class Cmd implements HokkanCommand {

  @Autowired
  private AccessControlService accessControlService;

  protected JSAP jsap;

  protected boolean channelOwnerOnly;
  protected boolean loggedInOnly;
  protected boolean privateOnly;
  protected boolean masterUserOnly;
  protected boolean toBotOnly;

  public Cmd() {
    jsap = new JSAP();
    jsap.setHelp("Help not set!");
  }

  protected void registerParameter(Option option) {
    try {
      jsap.registerParameter(option);
    } catch (JSAPException e) {
      e.printStackTrace();  // TODO handle?
    }
  }

  abstract public String getMatchPattern();

  public String getName() {
    String name = this.getClass().getSimpleName();
    if (name.endsWith("Cmd")) {
      name = name.replaceAll("Cmd", "");
    }
    return name;
  }

  public void handleLine(EngineRequest request, EngineResponse response) throws Exception {
    IrcMessageEvent ircEvent = (IrcMessageEvent) request.getIrcEvent();
    CommandArgs args = new CommandArgs(ircEvent.getMessage());

    if (args.hasArgs() && args.getArgs().equals("?")) {

      response.setResponseMessage("Usage: " + getName() + " " + jsap.getUsage() + "\n" + "Help: " + jsap.getHelp());

    } else {

      boolean parseRes;
      JSAPResult results = null;
      IDMap map = jsap.getIDMap();
      Iterator iterator = map.idIterator();
      String argsLine = args.joinArgs(1);
      if (iterator.hasNext()) {
        results = jsap.parse(argsLine);
        parseRes = results.success();
      } else {
        parseRes = true;
      }

      if (!parseRes) {
        response.setResponseMessage("Invalid arguments, usage: " + getName() + " " + jsap.getUsage());
      } else {
        if (checkAccess(request, response)) {
          handleRequest(request, response, results);
        }
      }

    }

  }

  public abstract void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException;

  private boolean checkAccess(EngineRequest request, EngineResponse response) {
    boolean isPublic = request.getIrcEvent() instanceof IrcMessageEvent;
    boolean masterUser = accessControlService.isMasterUser(request.getIrcEvent());

    if (isMasterUserOnly() && !masterUser) {
      response.setResponseMessage("Master user only command: " + getName());
      return false;
    }

    return true; // TODO
  }


  // ---------------------

  public boolean isChannelOwnerOnly() {
    return channelOwnerOnly;
  }

  public void setChannelOwnerOnly(boolean channelOwnerOnly) {
    this.channelOwnerOnly = channelOwnerOnly;
  }

  public boolean isLoggedInOnly() {
    return loggedInOnly;
  }

  public void setLoggedInOnly(boolean loggedInOnly) {
    this.loggedInOnly = loggedInOnly;
  }

  public boolean isPrivateOnly() {
    return privateOnly;
  }

  public void setPrivateOnly(boolean privateOnly) {
    this.privateOnly = privateOnly;
  }

  public boolean isMasterUserOnly() {
    return masterUserOnly;
  }

  public void setMasterUserOnly(boolean masterUserOnly) {
    this.masterUserOnly = masterUserOnly;
  }

  public boolean isToBotOnly() {
    return toBotOnly;
  }

  public void setToBotOnly(boolean toBotOnly) {
    this.toBotOnly = toBotOnly;
  }
}
