package com.freakz.hokan_ng.core_engine.command;

import com.freakz.hokan_ng.core_engine.command.handlers.Cmd;

import java.util.List;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 4:08 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface CommandHandlerService {

  Cmd getCommandHandler(String line);

  List<Cmd> getCommandHandlers();

  List<Cmd> getCommandHandlersByName(String name);

}
