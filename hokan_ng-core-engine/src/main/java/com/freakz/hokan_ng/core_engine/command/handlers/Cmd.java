package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.service.AccessControlService;
import com.freakz.hokan_ng.common.util.CommandArgs;
import com.martiansoftware.jsap.IDMap;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

  protected boolean isChannelOp;
  protected boolean isLoggedIn;
  protected boolean isPublic;
  protected boolean isPrivate;
  protected boolean isMasterUser;
  protected boolean isToBot;

  static protected Map<HelpGroup, List<Cmd>> helpGroups = new HashMap<>();

  public Cmd() {
    jsap = new JSAP();
    jsap.setHelp("Help not set!");
  }

  protected void registerParameter(Parameter paramter) {
    try {
      jsap.registerParameter(paramter);
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

  public void addToHelpGroup(HelpGroup helpGroup, Cmd cmd) {
    List<Cmd> cmds = this.helpGroups.get(helpGroup);
    if (cmds == null) {
      cmds = new ArrayList<>();
      this.helpGroups.put(helpGroup, cmds);
    }
    cmds.add(cmd);
  }

  public List<HelpGroup> getCmdHelpGroups(Cmd cmd) {
    List<HelpGroup> inGroups = new ArrayList<>();
    for (HelpGroup group : this.helpGroups.keySet()) {
      List<Cmd> cmds = this.helpGroups.get(group);
      if (cmds.contains(cmd)) {
        inGroups.add(group);
      }
    }
    return inGroups;
  }

  public List<Cmd> getOtherCmdsInGroup(HelpGroup group, Cmd cmd) {
    List<Cmd> otherCmds = new ArrayList<>();
    for (Cmd cmdInGroup : this.helpGroups.get(group)) {
      if (cmdInGroup != cmd) {
        otherCmds.add(cmdInGroup);
      }
    }
    return otherCmds;
  }

  public String getName() {
    String name = this.getClass().getSimpleName();
    if (name.endsWith("Cmd")) {
      name = name.replaceAll("Cmd", "");
    }
    return name;
  }

  protected String buildSeeAlso(Cmd cmd) {
    Comparator<Cmd> comparator = new Comparator<Cmd>() {
      @Override
      public int compare(Cmd cmd1, Cmd cmd2) {
        return cmd1.getName().compareTo(cmd2.getName());
      }
    };

    String seeAlsoGroups = "";
    for (HelpGroup group : getCmdHelpGroups(cmd)) {
      List<Cmd> groupCmds = getOtherCmdsInGroup(group, cmd);
      Collections.sort(groupCmds, comparator);
      if (groupCmds.size() > 0) {
        for (Cmd groupCmd : groupCmds) {
          if (groupCmd == this) {
            continue;
          }
          seeAlsoGroups += " " + groupCmd.getName();
        }
      }
    }
    String seeAlsoHelp = "";
    if (seeAlsoGroups.length() > 0) {
      seeAlsoHelp = "\nSee also:" + seeAlsoGroups;
    }
    return seeAlsoHelp;
  }

  public void handleLine(EngineRequest request, EngineResponse response) throws Exception {
    IrcMessageEvent ircEvent = (IrcMessageEvent) request.getIrcEvent();
    CommandArgs args = new CommandArgs(ircEvent.getMessage());

    if (args.hasArgs() && args.getArgs().equals("?")) {
      response.setResponseMessage("Usage: " + getName() + " " + jsap.getUsage() + "\n" + "Help: " + jsap.getHelp() + buildSeeAlso(this));

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
    InternalRequest ir = (InternalRequest) request;

    IrcMessageEvent ircMessageEvent = (IrcMessageEvent) request.getIrcEvent();
    isLoggedIn = false; // TODO
    isPublic = !ircMessageEvent.isPrivate();
    isPrivate = ircMessageEvent.isPrivate();
    isToBot = ircMessageEvent.isToMe();
    isMasterUser = accessControlService.isMasterUser(ircMessageEvent);
    isChannelOp = accessControlService.isChannelOp(ircMessageEvent, ir.getChannel());

    boolean ret = true;

    if (isToBotOnly() && !isToBot && !isMasterUser) {
      response.setResponseMessage("To bot only: " + getName());
      response.setReplyTo(ircMessageEvent.getSender());
      ret = false;
    }

    if (isPrivateOnly() && isPublic && !isMasterUser) {
      response.setResponseMessage("Private only: " + getName());
      response.setReplyTo(ircMessageEvent.getSender());
      ret = false;
    }

    if (isChannelOpOnly() && !isChannelOp && !isMasterUser) {
      response.setResponseMessage("ChannelOp only: " + getName());
      response.setReplyTo(ircMessageEvent.getSender());
      ret = false;
    }

    if (isMasterUserOnly() && !isMasterUser) {
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
