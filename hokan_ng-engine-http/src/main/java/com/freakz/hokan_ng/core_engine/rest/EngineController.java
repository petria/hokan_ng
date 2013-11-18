package com.freakz.hokan_ng.core_engine.rest;

import com.freakz.hokan_ng.common.exception.HokanEngineException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.core_engine.command.CommandHandlerService;
import com.freakz.hokan_ng.core_engine.command.handlers.Cmd;
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


  @RequestMapping(value = "/handle") //, produces = JSON, consumes = JSON)
  public
  @ResponseBody
  EngineResponse handleRequest(
      @RequestBody EngineRequest request
  ) {

    log.info("Got request: " + request);
    EngineResponse response = new EngineResponse(request);
    IrcMessageEvent ircMessageEvent = (IrcMessageEvent) request.getIrcEvent();
    Cmd handler = commandHandler.getCommandHandler(ircMessageEvent.getMessage());
    if (handler != null) {
      try {
        handler.handleLine(request, response);
      } catch (Exception e) {
        HokanEngineException engineException = new HokanEngineException(handler.getClass());
        response.setException(engineException);
        log.warn("Command handler returned exception {}", e.getMessage());
      }
    }
    return response;
  }

}
