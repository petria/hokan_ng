package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CHANNEL;

/**
 * User: petria
 * Date: 11/8/13
 * Time: 3:46 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class PartCmd extends Cmd {

  public PartCmd() {
    super();
    addToHelpGroup(HelpGroup.CHANNELS, this);


    UnflaggedOption uflg = new UnflaggedOption(ARG_CHANNEL)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(uflg);

    setMasterUserOnly(true);
  }

  @Override
  public String getMatchPattern() {
    return "!part.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String channel = results.getString(ARG_CHANNEL);
    response.setResponseMessage("Leaving: " + channel);
    response.addEngineMethodCall("partChannel", channel);
  }

}
