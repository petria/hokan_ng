package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/28/13
 * Time: 9:32 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class ExampleCmd extends Cmd {

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    response.addResponse("example jojjooo");
  }

}
