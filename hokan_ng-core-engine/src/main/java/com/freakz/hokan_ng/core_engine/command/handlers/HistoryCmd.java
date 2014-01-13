package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.engine.CommandHistory;
import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_NAME;

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
    addToHelpGroup(HelpGroup.PROCESS, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_NAME)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    List<CommandHistory> histories = commandPool.getCommandHistory();
    String name = results.getString(ARG_NAME, ".*");
    int c = 0;
    for (CommandHistory history : histories) {
      if (StringStuff.match(history.getRunnable().getClass().getName(), ".*" + name + ".*")) {
        response.addResponse("%s\n", history.toString());
        c++;
        if (c > 9) {
          break;
        }
      }
    }
  }

}
