package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
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
public class MasterUserOnlyCmd extends Cmd {

  public MasterUserOnlyCmd() {
    super();
    setMasterUserOnly(true);
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) {
    response.setResponseMessage("master user only reply");
  }

}
