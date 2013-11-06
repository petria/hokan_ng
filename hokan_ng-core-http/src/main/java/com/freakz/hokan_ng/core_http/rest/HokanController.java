package com.freakz.hokan_ng.core_http.rest;

import com.freakz.hokan_ng.core.exception.HokanException;
import com.freakz.hokan_ng.core.model.Connector;
import com.freakz.hokan_ng.core.service.ConnectionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date: 29.5.2013
 * Time: 09:31
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */

@Controller
public class HokanController {

  @Autowired
  ConnectionManagerService connectionManager;

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


}
