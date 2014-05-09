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
 * Created by pairio on 6.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class WhoQueryCmd extends Cmd {


  public WhoQueryCmd() {
    super();

    setHelp("Triggers WHO query on given channel. This refresh the internal user list of the channel.");
    addToHelpGroup(HelpGroup.SYSTEM, this);

    UnflaggedOption uflg = new UnflaggedOption(ARG_CHANNEL)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(uflg);

    setMasterUserOnly(true);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String channelName = results.getString(ARG_CHANNEL);
    response.addEngineMethodCall("sendWhoQuery", channelName);
    response.addResponse("Sending who query to: %s", channelName);
  }

}
