package com.freakz.hokan_ng.core_http.rest;

import com.freakz.hokan_ng.common.engine.Connector;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.CoreRequest;
import com.freakz.hokan_ng.common.rest.CoreResponse;
import com.freakz.hokan_ng.common.util.JarScriptExecutor;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.freakz.hokan_ng.core.service.ConnectionManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
public class HokanController {

  @Autowired
  private ConnectionManagerService connectionManager;

  @RequestMapping(value = "/control/handle") //, produces = JSON, consumes = JSON)
  public
  @ResponseBody
  CoreResponse handleRequest(
      @RequestBody CoreRequest request
  ) {
    log.info("Got request: " + request);
    CoreResponse response = new CoreResponse();
    connectionManager.handleCoreRequest(request);
    response.setRequest(request);

    return response;
  }


  @RequestMapping(value = "/control/joinChannels/{network}")
  public
  @ResponseBody
  String joinChannels(
      @PathVariable("network") String network
  ) {
    try {
      connectionManager.joinChannels(network);
    } catch (HokanException e) {
      return e.getMessage();
    }
    return "ok";
  }


  @RequestMapping(value = "/control/connect/{network}")
  public
  @ResponseBody
  String connect(
      @PathVariable("network") String network
  ) {
    try {
      connectionManager.connect(network);
    } catch (HokanException e) {
      return e.getMessage();
    }
    return "ok";
  }

  @RequestMapping(value = "/control/disconnect/{network}")
  public
  @ResponseBody
  String disconnect(
      @PathVariable("network") String network
  ) {
    try {
      connectionManager.disconnect(network);
    } catch (HokanException e) {
      return e.getMessage();
    }
    return "ok";
  }

  @RequestMapping(value = "/control/disconnectAll")
  public
  @ResponseBody
  String disconnectAll() {
    connectionManager.disconnectAll();
    return "ok";
  }

  @RequestMapping(value = "/control/connectors")
  public
  @ResponseBody
  String connectors() {
    String ret = "Connectors\n";
    for (Connector connector : connectionManager.getConnectors()) {
      ret += connector.toString() + "\n";
    }
    return ret;
  }

  @RequestMapping(value = "/control/test")
  public
  @ResponseBody
  String test() {
    JarScriptExecutor cmdExecutor = new JarScriptExecutor("/uptime.sh", "UTF-8");
    return StringStuff.arrayToString(cmdExecutor.executeJarScript(), " | ");
  }
}
