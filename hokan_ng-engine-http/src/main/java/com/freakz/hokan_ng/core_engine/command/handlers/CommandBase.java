package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 4:02 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public abstract class CommandBase implements HokkanCommand {

  protected static JSAP jsap;

  static {
    jsap = new JSAP();
    jsap.setHelp("Help not set!");
  }

  abstract public String getMatchPattern();

  public String getName() {
    return this.getClass().toString();
  }

  public String handleLine(EngineRequest request) {
    IrcEvent ircEvent = request.getIrcEvent();
    JSAPResult results = jsap.parse(ircEvent.getMessage());
    return handleRequest(request, results);
  }

  public abstract String handleRequest(EngineRequest request, JSAPResult results);

}
