package com.freakz.hokan_ng.core_http.rest;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.IrcServerConfigState;
import com.freakz.hokan_ng.core.service.IrcServerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Date: 10/31/13
 * Time: 11:38 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Controller
public class ConfigController {

  @Autowired
  IrcServerConfigService ircServerService;

  @RequestMapping(value = "/config/populateDatabase")
  public
  @ResponseBody
  String populateDatabase() throws Exception {
    List<IrcServerConfig> servers = ircServerService.getIrcServerConfigs();
    if (servers.size() == 0) {
      IrcServerConfig ircServerConfig = ircServerService.createIrcServerConfig("DevNET", "localhost", 6669, "1111", false, "#HokanDEV,#HokanDEV2", IrcServerConfigState.DISCONNECTED);
      return ircServerConfig.toString();
    }
    return "Servers already populated!";
  }

}
