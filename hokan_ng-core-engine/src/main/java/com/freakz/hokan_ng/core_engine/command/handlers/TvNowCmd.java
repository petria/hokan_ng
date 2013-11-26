package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 12:53 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class TvNowCmd extends Cmd {

  public TvNowCmd() {
    super();
    setHelp("Shows what's goign on in TV.");
  }

  @Override
  public String getMatchPattern() {
    return "!tvnow.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    response.addResponse("TODO");
  }
}
