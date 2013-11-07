package com.freakz.hokan_ng.core_engine.rest;

import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.UptimeService;
import com.freakz.hokan_ng.core_engine.command.CommandHandlerService;
import com.freakz.hokan_ng.core_engine.command.handlers.CommandBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date: 29.5.2013
 * Time: 09:31
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */

@Controller
@Slf4j
public class EngineController {


  @Autowired
  private CommandHandlerService commandHandler;

  @Autowired
  private UptimeService uptimeService;

  @RequestMapping(value = "/handle") //, produces = JSON, consumes = JSON)
  public
  @ResponseBody
  EngineResponse handleRequest(
      @RequestBody EngineRequest request
  ) {

    log.info("Got request: " + request);
    EngineResponse response = new EngineResponse(request);
    CommandBase handler = commandHandler.getCommandHandler(request.getIrcEvent().getMessage());
    if (handler != null) {
      String reply = handler.handleLine(request);
      response.setResponseMessage(reply);
    }
    return response;
  }

  @RequestMapping(value = "/test")
  public
  @ResponseBody
  String handleTest(
  ) {

    log.info("Got request");
    return "fufufuu";

  }

}
