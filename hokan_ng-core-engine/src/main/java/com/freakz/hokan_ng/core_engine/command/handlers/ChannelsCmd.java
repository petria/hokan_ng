package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 1/8/14
 * Time: 5:56 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class ChannelsCmd extends Cmd {

  @Autowired
  private ChannelService channelService;

  public ChannelsCmd() {
    super();
    setHelp("Shows IRC channels where the Bot has visited.");
    addToHelpGroup(HelpGroup.NETWORK, this);
    addToHelpGroup(HelpGroup.CHANNELS, this);
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    StringBuilder sb = new StringBuilder();
    List<Channel> channels = channelService.findChannels(ChannelState.ALL);
    if (channels.size() > 0) {
      for (Channel ch : channels) {
        sb.append("Name: ");
        sb.append(ch.getChannelName());
        sb.append(" ");
        sb.append("[");
        sb.append(ch.getChannelId());
        sb.append("]");
        sb.append(" @ ");
        sb.append(ch.getNetwork().getName());
        sb.append("\n");

        sb.append("  First joined        : ");
        sb.append(ch.getFirstJoined());
        sb.append("\n");

        sb.append("  Max user count      : ");
        sb.append(ch.getMaxUserCount());
        sb.append("\n");

        sb.append("  Max user count date : ");
        sb.append(ch.getMaxUserCountDate());
        sb.append("\n");

        sb.append("  Lines sent          : ");
        sb.append(ch.getLinesSent());
        sb.append("\n");

        sb.append("  Lines received      : ");
        sb.append(ch.getLinesReceived());
        sb.append("\n");

        sb.append("  Channel state       : ");
        sb.append(ch.getChannelState());
        sb.append("\n");

        sb.append("  Last active        : ");
        sb.append(ch.getLastActive());
        sb.append("\n");

        sb.append("  Last writer        : ");
        sb.append(ch.getLastWriter());
        sb.append("\n");

        sb.append("  Last writer spree  : ");
        sb.append(ch.getLastWriterSpree());
        sb.append("\n");

        sb.append("  Writer spree owner : ");
        sb.append(ch.getWriterSpreeOwner());
        sb.append("\n");

        sb.append("  Commands handled   : ");
        sb.append(ch.getCommandsHandled());
        sb.append("\n");
      }
    }

    response.addResponse(sb);
  }

}
