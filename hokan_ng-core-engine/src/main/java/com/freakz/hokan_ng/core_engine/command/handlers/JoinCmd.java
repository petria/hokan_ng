package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CHANNEL;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PASSWORD;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 3:18 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class JoinCmd extends Cmd {

  public JoinCmd() {
    super();
    addToHelpGroup(HelpGroup.CHANNELS, this);

    UnflaggedOption uflg = new UnflaggedOption(ARG_CHANNEL)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(uflg);

    uflg = new UnflaggedOption(ARG_PASSWORD)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(uflg);

    setMasterUserOnly(true);

  }

  @Override
  public String getMatchPattern() {
    return "!join.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) {
    String channel = results.getString(ARG_CHANNEL);
    String password = results.getString(ARG_PASSWORD);

    response.setResponseMessage("Trying to join to: " + channel);
    if (password != null) {
      response.addEngineMethodCall("joinChannel", channel, password);
    } else {
      response.addEngineMethodCall("joinChannel", channel);
    }
  }
}
