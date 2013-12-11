package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.engine.CommandHistory;
import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 12:18 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class HistoryCmd extends Cmd {

  @Autowired
  private CommandPool commandPool;

  public HistoryCmd() {
    super();
    setHelp("Show history of processes ran in Bot.");
    addSeeAlso("!ps");
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    List<CommandHistory> histories = commandPool.getCommandHistory();
    for (CommandHistory history : histories) {
      response.addResponse("%s\n", history.toString());
    }
  }

}
