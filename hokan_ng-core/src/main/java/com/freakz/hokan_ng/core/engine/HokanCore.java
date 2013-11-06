package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.core.model.EngineConnector;
import lombok.extern.slf4j.Slf4j;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 3.6.2013
 * Time: 9:15
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class HokanCore extends PircBot {

  private IrcServerConfig ircServerConfig;

  private EngineConnector engineConnector;

  private Map<String, String> serverProperties = new HashMap<>();

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

  public String toString() {
    return String.format("%s -> %s", this.getClass().toString(), this.ircServerConfig);
  }

  public void log(String message) {
    log.info(message);
  }

  // --- PircBot

  @Override
  protected void onDisconnect() {
    this.engineConnector.engineConnectorDisconnected(this);
  }

  @Override
  protected void onUnknown(String line) {
    log.info("UNKNOWN: {}", line);
  }

  @Override
  protected void onServerResponse(int code, String line) {
    if (code == 5) {
      String[] split = line.split(" ");
      for (String str : split) {
        if (str.contains("=")) {
          String[] keyValue = str.split("=");
          this.serverProperties.put(keyValue[0], keyValue[1]);
          log.info("--> {}: {}", keyValue[0], keyValue[1]);
        }
      }
    }
  }
}
