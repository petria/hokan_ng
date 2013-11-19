package com.freakz.hokan_ng.core_engine.command;

import com.freakz.hokan_ng.core_engine.command.handlers.Cmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 4:00 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class CommandHandlerServiceImpl implements CommandHandlerService {

  @Autowired
  private ApplicationContext context;

  private Map<String, Cmd> handlers;

  public CommandHandlerServiceImpl() {
  }

  @PostConstruct
  public void refreshHandlers() {
    handlers = context.getBeansOfType(Cmd.class);
  }

  @Override
  public Cmd getCommandHandler(String line) {
    for (Cmd base : this.handlers.values()) {
      if (line.matches(base.getMatchPattern())) {
        return base;
      }
    }
    return null;
  }
}
