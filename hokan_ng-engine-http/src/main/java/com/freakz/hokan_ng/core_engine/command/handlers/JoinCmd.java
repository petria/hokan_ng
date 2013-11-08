package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 3:18 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class JoinCmd extends Cmd {

  private static final String ARG_CHANNEL = "channel";
  private static final String ARG_PASSWORD = "password";

  public JoinCmd() {
    super();
    UnflaggedOption uflg = new UnflaggedOption(ARG_CHANNEL)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(uflg);

    uflg = new UnflaggedOption(ARG_PASSWORD)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(uflg);
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
