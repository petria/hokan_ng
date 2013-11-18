package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.util.CommandArgs;
import com.martiansoftware.jsap.IDMap;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Option;

import java.util.Iterator;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 4:02 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public abstract class Cmd implements HokkanCommand {

  protected JSAP jsap;

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
        handleRequest(request, response, results);
      }

    }

  }

  public abstract void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException;

}
