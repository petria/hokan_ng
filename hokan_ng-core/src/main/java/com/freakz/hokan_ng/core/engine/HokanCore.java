package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.freakz.hokan_ng.core.model.EngineConnector;
import lombok.extern.slf4j.Slf4j;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
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
public class HokanCore extends PircBot implements EngineEventHandler, DisposableBean {

  private IrcServerConfig ircServerConfig;

  private EngineConnector engineConnector;

  private EngineCommunicator engineCommunicator;

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

  @Autowired
  public void setEngineCommunicator(EngineCommunicator engineCommunicator) {
    this.engineCommunicator = engineCommunicator;
  }

  public IrcServerConfig getIrcServerConfig() {
    return ircServerConfig;
  }

  public void setIrcServerConfig(IrcServerConfig ircServerConfig) {
    this.ircServerConfig = ircServerConfig;
  }

  public String toString() {
    return String.format("%s -> %s", this.getClass().toString(), this.ircServerConfig);
  }

  public void log(String message) {
    if (!message.contains("PING") && !message.contains("PONG")) {
      log.info(message);
    }
  }

  @Override
  public void destroy() throws Exception {
    List<Runnable> runnableList = executor.shutdownNow();
    log.info("Runnables size: {}", runnableList.size());
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

  @Override
  protected void onMessage(String channel, String sender, String login, String hostname, String message) {
    IrcEvent ircEvent = IrcEvent.create(channel, sender, login, hostname, message);
    EngineRequest request = new EngineRequest(ircEvent);

    if (message.startsWith("!")) {
      this.engineCommunicator.sendEngineMessage(request, this);
    }
  }

  @Override
  public void handleEngineResponse(EngineResponse response) {
    sendMessage(response.getRequest().getIrcEvent().getChannel(), response.getResponseMessage());
    if (response.getEngineMethod() != null) {
      log.info("Executing engine method : " + response.getEngineMethod());
      log.info("Engine method args      : " + response.getEngineMethodArgs());
      Class clazz = this.getClass();
      Method[] methods = clazz.getMethods();
      for (Method method : methods) {
        if (method.getName().equals(response.getEngineMethod())) {
          try {
            if (method.getParameterTypes().length == response.getEngineMethodArgs().length) {
              log.info("Invoking method         : " + method);
              method.invoke(this, response.getEngineMethodArgs());
            }
          } catch (Exception e) {
            log.error("Couldn't do engine method!", e);
          }
        }
      }
    }

    log.info("engine response: " + response.getResponseMessage());
  }

}
