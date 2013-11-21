package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.core_engine.command.CommandHandlerService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: petria
 * Date: 11/21/13
 * Time: 1:55 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class HelpCmd extends Cmd {

  @Autowired
  private CommandHandlerService commandHandler;

  private final static String ARG_COMMAND = "Command";

  public HelpCmd() {
    super();
    UnflaggedOption flg = new UnflaggedOption(ARG_COMMAND)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);
  }

  @Override
  public String getMatchPattern() {
    return "!help.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String command = results.getString(ARG_COMMAND);

    StringBuilder sb = new StringBuilder();

//    boolean isSuperUser = UserManager.isSuperUser(iEvent.getHokanUser(), iEvent);

    if (command == null) {
      List<Cmd> commands = commandHandler.getCommandHandlers();
      Comparator<Cmd> comparator = new Comparator<Cmd>() {
        @Override
        public int compare(Cmd cmd1, Cmd cmd2) {
          return cmd1.getName().compareTo(cmd2.getName());
        }
      };
      Collections.sort(commands, comparator);

      sb.append("== HELP: Command List");
      sb.append("\n");

      for (Cmd cmd : commands) {
        sb.append("  ");
        sb.append(cmd.getName());
      }

      sb.append("\nTry '!help <command>' to get detailed help\n");

    } else {
      List<Cmd> commands = commandHandler.getCommandHandlersByName(command);
      Comparator<Cmd> comparator = new Comparator<Cmd>() {
        @Override
        public int compare(Cmd cmd1, Cmd cmd2) {
          return cmd1.getName().compareTo(cmd2.getName());
        }
      };
      Collections.sort(commands, comparator);
      for (Cmd cmd : commands) {
        String help = String.format("HELP: %s (%s) -> %s", cmd.getName(), cmd.getMatchPattern(), cmd.getHelp());
        sb.append(help);
        sb.append("\n");
      }
    }
    response.setResponseMessage(sb.toString());

  }
}
