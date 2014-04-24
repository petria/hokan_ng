package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 3:59 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class PrivateOnlyCmd extends Cmd {

  public PrivateOnlyCmd() {
    super();
    setPrivateOnly(true);
  }

  @Override
  public String getMatchPattern() {
    return "!privateonly.*";
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) {
    response.setResponseMessage("private only reply");
  }

}
