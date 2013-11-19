package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.ChannelUser;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.freakz.hokan_ng.common.service.ChannelUsersService;
import com.freakz.hokan_ng.common.service.NetworkService;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 03-Mar-2009
 * Time: 09:08:27
 *
 * @author Petri Airio (petri.j.airio@gmail.com) *
 */
@Component
public class JoinedCmd extends Cmd {

  @Autowired
  private ChannelService channelService;

  @Autowired
  private ChannelUsersService channelUsersService;

  @Autowired
  private NetworkService networkService;

  public JoinedCmd() {
    super();
  }


  public String getMatchPattern() {
    return "!joined";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    StringBuilder sb = new StringBuilder();
    List<Network> networks = networkService.getNetworks();
    for (Network network : networks) {
      List<Channel> channels = channelService.findChannels(network, ChannelState.JOINED);
      if (channels.size() > 0) {
        sb.append(String.format("[%s]\n", network.getName()));
        for (Channel channel : channels) {
          if (channels.size() > 0) {
            sb.append(String.format("  %s (%d)\n", channel.getChannelName(), channel.getChannelId()));
            List<ChannelUser> channelUsers = channelUsersService.findChannelUsers(channel);
            sb.append("    ");
            for (ChannelUser channelUser : channelUsers) {
              sb.append(channelUser.getUser().getNick());
              sb.append(" ");
            }
            sb.append("\n");
          }
        }
      }
    }
    response.setResponseMessage(sb.toString());
  }
}
