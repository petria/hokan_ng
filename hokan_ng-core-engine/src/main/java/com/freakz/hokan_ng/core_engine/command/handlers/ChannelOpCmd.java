package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 3:59 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class ChannelOpCmd extends Cmd {

  public ChannelOpCmd() {
    super();
    setChannelOpOnly(true);
  }

  @Override
  public String getMatchPattern() {
    return "!channelop.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) {
    response.setResponseMessage("channel op only reply");
  }

}
