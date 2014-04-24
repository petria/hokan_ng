package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CHANNEL;

/**
 * User: petria
 * Date: 12/19/13
 * Time: 7:25 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class OpCmd extends Cmd {

  public OpCmd() {
    super();
    setHelp("Gives operator rights to channel.");
    addToHelpGroup(HelpGroup.CHANNELS, this);
    addToHelpGroup(HelpGroup.USERS, this);
    setLoggedInOnly(true);

    UnflaggedOption flg = new UnflaggedOption(ARG_CHANNEL)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    String channel = results.getString(ARG_CHANNEL);
    response.addEngineMethodCall("op", channel, ir.getIrcEvent().getSender());
    response.addResponse("Trying to op %s on %s", ir.getIrcEvent().getSender(), channel);
  }

}
