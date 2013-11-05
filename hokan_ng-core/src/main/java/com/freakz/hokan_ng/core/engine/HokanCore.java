package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.core.model.EngineConnector;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3.6.2013
 * Time: 9:15
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
public class HokanCore extends PircBot {

  private IrcServerConfig ircServerConfig;

  private EngineConnector engineConnector;

  public HokanCore() {
  }

  public void init(String botName, IrcServerConfig ircServerConfig) {

    this.ircServerConfig = ircServerConfig;

    setVerbose(true);
    setName(botName);
    setVersion("Hokan NG");
    setLogin("hokan");

    setMessageDelay(1100);

  }

  @Autowired
  public void setEngineConnector(EngineConnector engineConnector) {
    this.engineConnector = engineConnector;
  }


  public IrcServerConfig getIrcServerConfig() {
    return ircServerConfig;
  }

  @Override
  protected void onDisconnect() {
    this.engineConnector.engineConnectorDisconnected(this);
  }

  public String toString() {
    return String.format("%s -> %s", this.getClass().toString(), this.ircServerConfig);
  }

}
