package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/28/13
 * Time: 9:32 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class ExampleCmd extends Cmd {

  public ExampleCmd() {
    super();
    setHelp("Example moi");
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    response.addResponse("moi");
  }
}
