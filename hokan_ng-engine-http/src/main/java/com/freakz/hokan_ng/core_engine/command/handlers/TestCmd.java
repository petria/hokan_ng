package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.martiansoftware.jsap.JSAPResult;
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

  public TestCmd() {
    super();
  }

  @Override
  public String getMatchPattern() {
    return "!test.*";
  }

  public String getName() {
    return "Test";
  }

  @Override
  public String handleRequest(EngineRequest request, JSAPResult results) {
    return "test command reply";
  }

}
