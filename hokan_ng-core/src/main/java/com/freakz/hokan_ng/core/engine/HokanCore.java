package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineMethodCall;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.freakz.hokan_ng.core.model.EngineConnector;
import lombok.extern.slf4j.Slf4j;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Date;
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

  @Autowired
  private ChannelService channelService;

  private IrcServerConfig ircServerConfig;

  private EngineConnector engineConnector;

  private EngineCommunicator engineCommunicator;

  private Map<String, String> serverProperties = new HashMap<>();
  private Map<String, Method> methodMap = new HashMap<>();

  public HokanCore() {
    Class clazz = this.getClass();
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      methodMap.put(method.getName(), method);
    }
    log.info("Built method map, size {}", methodMap.size());
  }

  public void init(String botName, IrcServerConfig ircServerConfig) {

    this.ircServerConfig = ircServerConfig;

    setVerbose(true);
    setName(botName);
    setVersion("Hokan NG");
    setLogin("hokan");

    setMessageDelay(1100);

  }

  private Method getEngineMethod(String name, int args) {
    for (Method method : methodMap.values()) {
      if (method.getName().equals(name) && method.getGenericParameterTypes().length == args) {
        return method;
      }
    }
    return null;
  }

  @PostConstruct
  public void postInit() throws HokanException {

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
  // ---

  public Network getNetwork() {
    return this.getIrcServerConfig().getNetwork();
  }

  public Channel getChannel(IrcEvent ircEvent) {
    Channel channel = channelService.findChannelByName(getNetwork(), ircEvent.getChannel());
    if (channel == null) {
      channel = channelService.createChannel(getNetwork(), ircEvent.getChannel());
    }
    channel.setLastActive(new Date());
    return channel;
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
    Channel ch = getChannel(ircEvent);
    ch.setLastWriter(sender);
    ch.addToLinesReceived(1);

    EngineRequest request = new EngineRequest(ircEvent);
    this.engineCommunicator.sendEngineMessage(request, this);
    this.channelService.updateChannel(ch);
  }

  @Override
  protected void onPrivateMessage(String sender, String login, String hostname, String message) {
    log.info("Message: {}", message);
  }

  @Override
  @SuppressWarnings({"varargs"})
  public void handleEngineResponse(EngineResponse response) {

    Channel ch = getChannel(response.getRequest().getIrcEvent());
    ch.addCommandsHandled(1);

    sendMessage(response.getRequest().getIrcEvent().getChannel(), response.getResponseMessage());
    for (EngineMethodCall methodCall : response.getEngineMethodCalls()) {
      String methodName = methodCall.getMethodName();
      String[] methodArgs = methodCall.getMethodArgs();

      log.info("Executing engine method : " + methodName);
      log.info("Engine method args      : " + StringStuff.arrayToString(methodArgs, ", "));
      Method method = getEngineMethod(methodName, methodArgs.length);
      if (method != null) {
        try {
          if (method.getParameterTypes().length == methodArgs.length) {
            log.info("Invoking method         : " + method);
            method.invoke(this, methodArgs);
          }
        } catch (Exception e) {
          log.error("Couldn't do engine method!", e);
        }
      } else {
        log.error("Couldn't find method for: " + methodName);
      }

    }

    log.info("engine response: " + response.getResponseMessage());
  }

  @Override
  protected void onJoin(String channel, String sender, String login, String hostname) {
    Channel ch = getChannel(IrcEvent.create(channel, sender, login, hostname));
    log.info("{} joined channel: {}", sender, channel);
    if (sender.equalsIgnoreCase(getNick())) {
      ch.setChannelState(ChannelState.JOINED);
      if (ch.getFirstJoined() == null) {
        ch.setFirstJoined(new Date());
      }
    }
    this.channelService.updateChannel(ch);
  }

  @Override
  protected void onPart(String channel, String sender, String login, String hostname) {
    Channel ch = getChannel(IrcEvent.create(channel, sender, login, hostname));
    log.info("{} part channel: {}", sender, channel);
    if (sender.equalsIgnoreCase(getNick())) {
      ch.setChannelState(ChannelState.NOT_JOINED);
    }
    this.channelService.updateChannel(ch);
  }
}
