package com.freakz.hokan_ng.core_engine.rest;

import com.freakz.hokan_ng.commmon.rest.EngineRequest;
import com.freakz.hokan_ng.commmon.rest.EngineResponse;
import lombok.extern.slf4j.Slf4j;
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

  public static final String JSON = "application/json";

  @RequestMapping(value = "/handle", produces = JSON, consumes = JSON)
  public
  @ResponseBody
  EngineResponse handleRequest(
      @RequestBody EngineRequest request
  ) {

    log.info("Got request: " + request);
    EngineResponse response = new EngineResponse("hello: " + request.getRequest());
    try {
      Thread.sleep(10 * 1000);
    } catch (InterruptedException e) {
      // ignore
    }
    return response;

  }

  @RequestMapping(value = "/test", produces = JSON, consumes = JSON)
  public
  @ResponseBody
  String handleTest(
  ) {

    log.info("Got request");
    return "fufufuu";

  }

}
