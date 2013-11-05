package com.freakz.hokan_ng.core_http.rest;

import com.freakz.hokan_ng.core.service.ConnectionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

  @RequestMapping(value = "/control/goOnline")
  public
  @ResponseBody
  String goOnline() throws Exception {
    connectionManager.goOnline("DevNET");
    return "ok";
  }

  @RequestMapping(value = "/control/disconnectAll")
  public
  @ResponseBody
  String disconnectAll() throws Exception {
    connectionManager.disconnectAll();
    return "ok";
  }

}
