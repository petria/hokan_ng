package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.IrcEvent;
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
public abstract class CommandBase implements HokkanCommand {

  protected JSAP jsap;

  public CommandBase() {
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
    return this.getClass().toString();
  }

  public String handleLine(EngineRequest request) {
    IrcEvent ircEvent = request.getIrcEvent();
    JSAPResult results = jsap.parse(ircEvent.getMessage());
    CommandArgs args = new CommandArgs(ircEvent.getMessage());

    if (args.hasArgs() && args.getArgs().equals("?")) {

      return "Usage: " + getName() + " " + jsap.getUsage() + "\n" + "Help: " + jsap.getHelp();

    } else {

      boolean parseRes;

      IDMap map = jsap.getIDMap();
      Iterator iterator = map.idIterator();
      if (iterator.hasNext()) {
        results = jsap.parse(ircEvent.getMessage());
        parseRes = results.success();
      } else {
        parseRes = true;
      }

      if (!parseRes) {

        return "Invalid arguments, usage: " + getName() + " " + jsap.getUsage();
      }

      return handleRequest(request, results);

    }

  }

  public abstract String handleRequest(EngineRequest request, JSAPResult results);

}
