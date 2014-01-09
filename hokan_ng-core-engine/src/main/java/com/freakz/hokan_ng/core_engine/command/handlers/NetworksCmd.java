package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.NetworkService;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 1/8/14
 * Time: 5:37 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class NetworksCmd extends Cmd {

  @Autowired
  private NetworkService networkService;

  public NetworksCmd() {
    super();
    setHelp("Shows configured IRC Networks");
    addToHelpGroup(HelpGroup.NETWORK, this);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    StringBuilder sb = new StringBuilder();
    List<Network> nws = networkService.getNetworks();
    if (nws.size() > 0) {
      for (Network nw : nws) {
        sb.append("Name: ");
        sb.append(nw.getName());
        sb.append("\n");

        sb.append("  First connected : ");
        sb.append(nw.getFirstConnected());
        sb.append("\n");

        sb.append("  Connect count   : ");
        sb.append(nw.getConnectCount());
        sb.append("\n");

        sb.append("  Lines sent      : ");
        sb.append(nw.getLinesSent());
        sb.append("\n");

        sb.append("  Lines received  : ");
        sb.append(nw.getLinesReceived());
        sb.append("\n");

        sb.append("  Channels joined : ");
        sb.append(nw.getChannelsJoined());
        sb.append("\n");
      }

      response.addResponse(sb);
    }
  }

}
