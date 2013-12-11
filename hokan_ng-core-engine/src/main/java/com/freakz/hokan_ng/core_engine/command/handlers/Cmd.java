package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 4:02 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
public abstract class Cmd implements HokkanCommand, CommandRunnable {

  @Autowired
  private AccessControlService accessControlService;

  @Autowired
  private CommandPool commandPool;

  protected JSAP jsap;

  protected boolean channelOpOnly;
  protected boolean loggedInOnly;
  protected boolean privateOnly;
  protected boolean masterUserOnly;
  protected boolean toBotOnly;

  protected List<String> seeAlso = new ArrayList<>();

  public Cmd() {
    jsap = new JSAP();
    jsap.setHelp("Help not set!");
  }

  protected void registerParameter(Option option) {
    try {
      jsap.registerParameter(option);
    } catch (JSAPException e) {
      log.error("Error registering command parameter", e);
    }
  }

  public String getMatchPattern() {
    return String.format("!%s.*", getName().toLowerCase());
  }

  public void setHelp(String helpText) {
    this.jsap.setHelp(helpText);
  }

  public List<String> getSeeAlso() {
    return seeAlso;
  }

  public void addSeeAlso(String seeAlso) {
    this.seeAlso.add(seeAlso);
  }

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
      String seeAlsoHelp = "";
      for (String seeAlsoTxt : getSeeAlso()) {
        if (seeAlsoHelp.length() > 0) {
          seeAlsoHelp += ", ";
        }
        seeAlsoHelp += seeAlsoTxt;
      }

      response.setResponseMessage("Usage: " + getName() + " " + jsap.getUsage() + "\n" + "Help: " + jsap.getHelp() + "\nSee also: " + seeAlsoHelp);

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
          ArgsWrapper wrapper = new ArgsWrapper();
          wrapper.request = request;
          wrapper.response = response;
          wrapper.results = results;
          commandPool.startSyncRunnable(this, wrapper);
//          handleRequest(request, response, results);
        }
      }

    }

  }

  public static class ArgsWrapper {
    public EngineRequest request;
    public EngineResponse response;
    public JSAPResult results;
  }

  @Override
  public void handleRun(long myPid, Object args) throws HokanException {
    ArgsWrapper wrapper = (ArgsWrapper) ((Object[]) args)[0];
    handleRequest(wrapper.request, wrapper.response, wrapper.results);
  }

  public abstract void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException;

  private boolean checkAccess(EngineRequest request, EngineResponse response) {
    IrcMessageEvent ircMessageEvent = (IrcMessageEvent) request.getIrcEvent();
    boolean isPublic = !ircMessageEvent.isPrivate();
    boolean isToMe = ircMessageEvent.isToMe();
    boolean masterUser = accessControlService.isMasterUser(ircMessageEvent);
    boolean channelOp = accessControlService.isChannelOp(ircMessageEvent);

    boolean ret = true;

    if (isToBotOnly() && !isToMe && !masterUser) {
      response.setResponseMessage("To bot only: " + getName());
      response.setReplyTo(ircMessageEvent.getSender());
      ret = false;
    }

    if (isPrivateOnly() && isPublic && !masterUser) {
      response.setResponseMessage("Private only: " + getName());
      response.setReplyTo(ircMessageEvent.getSender());
      ret = false;
    }

    if (isChannelOpOnly() && !channelOp && !masterUser) {
      response.setResponseMessage("ChannelOp only: " + getName());
      response.setReplyTo(ircMessageEvent.getSender());
      ret = false;
    }

    if (isMasterUserOnly() && !masterUser) {
      response.setResponseMessage("Master user only: " + getName());
      response.setReplyTo(ircMessageEvent.getSender());
      ret = false;
    }
    return ret;
  }

  // ---------------------

  public boolean isChannelOpOnly() {
    return channelOpOnly;
  }

  public void setChannelOpOnly(boolean channelOpOnly) {
    this.channelOpOnly = channelOpOnly;
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

  public String getHelp() {
    return this.jsap.getHelp();
  }

}
