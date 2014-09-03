package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.core_engine.command.CommandHandlerService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_COMMAND;

/**
 * User: petria
 * Date: 11/21/13
 * Time: 1:55 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class HelpCmd extends Cmd {

  @Autowired
  private CommandHandlerService commandHandler;

  public HelpCmd() {
    super();
    setHelp("Shows command list / help about specific command.");
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
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String command = results.getString(ARG_COMMAND);

    StringBuilder sb = new StringBuilder();

    Comparator<Cmd> comparator = new Comparator<Cmd>() {
      @Override
      public int compare(Cmd cmd1, Cmd cmd2) {
        return cmd1.getName().compareTo(cmd2.getName());
      }
    };

    if (command == null) {

      List<Cmd> commands = commandHandler.getCommandHandlers();
      Collections.sort(commands, comparator);

      sb.append("== HELP: Command List");
      sb.append("\n");

      for (Cmd cmd : commands) {

        if (cmd.isChannelOpOnly() && !isChannelOp && !isMasterUser) {
          continue;
        }

        if (cmd.isMasterUserOnly() && (!isMasterUser)) {
          continue;
        }

        sb.append("  ");
        sb.append(cmd.getName());
        String flags = "";
        if (cmd.toBotOnly) {
          flags += "B";
        }
        if (cmd.channelOpOnly) {
          flags += "C";
        }
        if (cmd.loggedInOnly) {
          flags += "L";
        }
        if (cmd.masterUserOnly) {
          flags += "M";
        }
        if (cmd.privateOnly) {
          flags += "P";
        }
        if (flags.length() > 0) {
          sb.append("[").append(flags).append("]");
        }
      }
      sb.append("\nTry '!help <command>' to get detailed help\n");
      sb.append("B: to bot only ");
      if (isMasterUser || isChannelOp) {
        sb.append("C: channel op only ");
      }
      sb.append("L: logged in only ");
      if (isMasterUser) {
        sb.append("M: master user only ");
      }
      sb.append("P: private msg only");

    } else {
      List<Cmd> commands = commandHandler.getCommandHandlersByName(command);
      Collections.sort(commands, comparator);
      for (Cmd cmd : commands) {
        String help = String.format("HELP: %s (%s) -> %s%s",
            cmd.getName(), cmd.getMatchPattern(), cmd.getHelp(), buildSeeAlso(cmd));
        sb.append(help);
        sb.append("\n");
      }
    }
    response.setResponseMessage(sb.toString());

  }
}
