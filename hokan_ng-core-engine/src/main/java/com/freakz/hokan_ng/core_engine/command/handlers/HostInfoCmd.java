package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.util.JarScriptExecutor;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 12/13/13
 * Time: 9:33 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class HostInfoCmd extends Cmd {

  public HostInfoCmd() {
    super();
    setHelp("Shows information about the host machine where the Bot is running on.");
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    JarScriptExecutor cmdExecutor = new JarScriptExecutor("/hostinfo.sh", "UTF-8");
    String[] hostinfo = cmdExecutor.executeJarScript();

    response.addResponse("I am running on: %s", hostinfo[0]);
  }

}
