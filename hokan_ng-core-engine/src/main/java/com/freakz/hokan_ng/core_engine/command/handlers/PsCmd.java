package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunner;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 11:35 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class PsCmd extends Cmd {

  @Autowired
  private CommandPool commandPool;

  public PsCmd() {
    super();
    setHelp("Shows active processes running in Bot.");
    addToHelpGroup(HelpGroup.PROCESS, this);
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    List<CommandRunner> runners = commandPool.getActiveRunners();
    for (CommandRunner runner : runners) {
      response.addResponse("%s\n", runner.toString());
    }
  }
}
