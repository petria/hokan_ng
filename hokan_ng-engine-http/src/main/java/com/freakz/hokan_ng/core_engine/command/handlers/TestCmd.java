package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.IrcEvent;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 3:59 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class TestCmd extends CommandBase {


  @Override
  public String getMatchPattern() {
    return "!test.*";
  }

  public String handleLine(IrcEvent ircEvent) {
    return "TestCmd reply";
  }

}
