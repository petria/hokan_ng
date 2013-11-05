package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.core.service.ConnectionManagerService;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Date: 3.6.2013
 * Time: 9:15
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
public class HokanCore extends PircBot {

  @Autowired
  ConnectionManagerService connectionManager;

  private final IrcServerConfig ircServerConfig;

  public HokanCore(String botName, IrcServerConfig ircServerConfig) {

    this.ircServerConfig = ircServerConfig;

    setVerbose(true);
    setName(botName);
    setVersion("Hokan NG"); // real name
    setLogin("hokan_ng");

    setMessageDelay(1100);

  }

  public IrcServerConfig getIrcServerConfig() {
    return ircServerConfig;
  }

}
