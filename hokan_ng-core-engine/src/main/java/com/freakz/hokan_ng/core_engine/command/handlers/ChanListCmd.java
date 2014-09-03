package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/5/13
 * Time: 9:24 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class ChanListCmd extends Cmd {

  @Autowired
  private ChannelService channelService;

  public ChanListCmd() {
    super();
    setHelp("Shows what channels the Bot is joined.");
    addToHelpGroup(HelpGroup.CHANNELS, this);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    List<Channel> channels = channelService.findChannels(ChannelState.ALL);
    StringBuilder sb = new StringBuilder();
    for (Channel channel : channels) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      Network network = channel.getNetwork();
      String ch = String.format("[%d] %s(%s): %s",
          channel.getChannelId(),
          channel.getChannelName(),
          network.getName(),
          channel.getChannelState());
      sb.append(ch);
    }
    response.addResponse("Known channels: ");
    response.addResponse(sb.toString());
  }
}
