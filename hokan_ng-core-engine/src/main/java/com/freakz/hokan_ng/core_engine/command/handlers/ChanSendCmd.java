package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CHAN_ID;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_MESSAGE;

/**
 * User: petria
 * Date: 12/5/13
 * Time: 10:01 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class ChanSendCmd extends Cmd {

  @Autowired
  private ChannelService channelService;


  public ChanSendCmd() {
    super();
    setHelp("Sends message to specified channel.");
    addToHelpGroup(HelpGroup.CHANNELS, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_CHAN_ID)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    flg = new UnflaggedOption(ARG_MESSAGE)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    setChannelOpOnly(true);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String idStr = results.getString(ARG_CHAN_ID);
    long id = -1;
    try {
      id = Long.parseLong(idStr);
    } catch (NumberFormatException e) {
      response.addResponse("Illegal id: " + id);
      return;
    }
    Channel channel = channelService.findChannelById(id);
    if (channel == null) {
      response.addResponse("Unknown channel: " + id);
      return;
    }
    response.addResponse("Sending message to: " + channel.getChannelName());
    response.addEngineMethodCall("sendMessage", channel.getChannelName(), results.getString(ARG_MESSAGE));
  }
}
