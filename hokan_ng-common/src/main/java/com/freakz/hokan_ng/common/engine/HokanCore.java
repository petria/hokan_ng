package com.freakz.hokan_ng.common.engine;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineMethodCall;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.freakz.hokan_ng.common.rest.IrcEventFactory;
import com.freakz.hokan_ng.common.rest.IrcPrivateMessageEvent;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.freakz.hokan_ng.common.service.ChannelUsersService;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

  @Autowired
  private UserChannelService userChannelService;

  @Autowired
  private UserService userService;

  @Autowired
  private ChannelUsersService channelUsersService;

  //--------

  private IrcServerConfig ircServerConfig;

  private EngineConnector engineConnector;

  private EngineCommunicator engineCommunicator;

  private Map<String, String> serverProperties = new HashMap<>();
  private Map<String, Method> methodMap = new HashMap<>();
  private Map<String, List<String>> whoQueries = new HashMap<>();

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

  public Channel getChannel(String channelName) {
    Channel channel = channelService.findChannelByName(getNetwork(), channelName);
    if (channel == null) {
      channel = channelService.createChannel(getNetwork(), channelName);
    }
//    channel.setLastActive(new Date());
    return channel;
  }

  public Channel getChannel(IrcEvent ircEvent) {
    return getChannel(ircEvent.getChannel());
  }

  public User getUser(IrcEvent ircEvent) {
    try {
      User user = this.userService.findUser(ircEvent.getSender());
      return user;
    } catch (HokanException e) {
      coreExceptionHandler(e);
    }
/*    if (user == null) {
      user = new User();
      user.setNick(split[5]);
      user.setMask(StringStuff.quoteRegExp(mask));
      user.setPassword("1234");
      user.setFullName(fullName);
      user = this.userService.updateUser(user);
    }*/
    return null;
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

  private void sendWhoQuery(String channel) {
    log.info("Sending WHO query to: " + channel);
    List<String> whoReplies = new ArrayList<>();
    whoQueries.put(channel.toLowerCase(), whoReplies);
    sendRawLineViaQueue("WHO " + channel);
  }

  @Override
  protected void onUserList(String channel, org.jibble.pircbot.User[] users) {
    sendWhoQuery(channel);
  }

  private void handleWhoList(String channelName, List<String> whoReplies) throws HokanException {
    Channel channel = getChannel(channelName);
    this.channelUsersService.clearChannelUsers(channel);
    for (String whoLine : whoReplies) {
      String[] split = whoLine.split(" ");
      String nick = split[5];
      String mask = split[5] + "!" + split[2] + "@" + split[3];
      String fullName = StringStuff.joinStringArray(split, 8);
      User user = this.userService.findUser(nick);
      if (user == null) {
        user = new User();
        user.setNick(split[5]);
        user.setMask(StringStuff.quoteRegExp(mask));
        user.setPassword("1234");
        user.setFullName(fullName);
        user = this.userService.updateUser(user);
      }
      UserChannel userChannel = userChannelService.getUserChannel(user, channel);
      if (userChannel == null) {
        userChannelService.createUserChannel(user, channel);
      }
      this.channelUsersService.createChannelUser(channel, user);
    }
  }

  @Override
  protected void onServerResponse(int code, String line) {
    if (code == RPL_WHOREPLY) {
      String[] split = line.split(" ");
      if (split.length >= 6) {
        String channel = split[1];
        List<String> whoReplies = whoQueries.get(channel.toLowerCase());
        whoReplies.add(line);
      } else {
        log.info("SKIPPED WHO REPLY: {}", line);
      }

    } else if (code == RPL_ENDOFWHO) {
      String[] split = line.split(" ");
      String channel = split[1];
      List<String> whoReplies = this.whoQueries.remove(channel.toLowerCase());
      try {
        handleWhoList(channel, whoReplies);
      } catch (HokanException e) {
        coreExceptionHandler(e);
      }
      log.info("Handled {} WHO lines!", whoReplies.size());

    }
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

  private void coreExceptionHandler(HokanException e) {
    log.error("---------------------------");
    log.error("Exception", e);
    log.error("---------------------------");
  }

  @Override
  protected void onMessage(String channel, String sender, String login, String hostname, String message) {
    IrcEvent ircEvent = IrcEventFactory.createIrcMessageEvent(channel, sender, login, hostname, message);
    Channel ch = getChannel(ircEvent);
    ch.setLastWriter(sender);
    ch.addToLinesReceived(1);

    EngineRequest request = new EngineRequest(ircEvent);
    this.engineCommunicator.sendEngineMessage(request, this);
    this.channelService.updateChannel(ch);
  }

  @Override
  protected void onPrivateMessage(String sender, String login, String hostname, String message) {
    IrcPrivateMessageEvent ircEvent = (IrcPrivateMessageEvent) IrcEventFactory.createIrcPrivateMessageEvent(sender, login, hostname, message);
    log.info("Message: {}", ircEvent.getMessage());
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
    IrcEvent ircEvent = IrcEventFactory.createIrcEvent(channel, sender, login, hostname);
    Channel ch = getChannel(ircEvent);
    log.info("{} joined channel: {}", sender, channel);
    if (sender.equalsIgnoreCase(getNick())) {
      ch.setChannelState(ChannelState.JOINED);
      if (ch.getFirstJoined() == null) {
        ch.setFirstJoined(new Date());
      }
    } else {
      this.channelUsersService.createChannelUser(ch, getUser(ircEvent));
    }
    this.channelService.updateChannel(ch);
  }

  @Override
  protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    sendWhoQuery(channel);
/*    IrcEvent ircEvent = IrcEvent.create(channel, sender, login, hostname);
    Channel ch = getChannel(IrcEvent.create(channel, sender, login, hostname));
    super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);    //To change body of overridden methods use File | Settings | File Templates.*/
  }

  @Override
  protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason, String[] fromChannels) {
    for (String channel : fromChannels) {
      sendWhoQuery(channel);
    }
  }

  @Override
  protected void onPart(String channel, String sender, String login, String hostname) {
    IrcEvent ircEvent = IrcEventFactory.createIrcEvent(channel, sender, login, hostname);
    Channel ch = getChannel(ircEvent);
    log.info("{} part channel: {}", sender, channel);
    if (sender.equalsIgnoreCase(getNick())) {
      ch.setChannelState(ChannelState.NOT_JOINED);
      this.channelUsersService.clearChannelUsers(ch);
    } else {
      this.channelUsersService.removeChannelUser(ch, getUser(ircEvent));
    }
    this.channelService.updateChannel(ch);
  }

  @Override
  protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
    for (String joined : getChannels()) {
      sendWhoQuery(joined);
    }

/*    IrcEvent ircEvent = IrcEvent.create(channel, sender, login, hostname);
    Channel ch = getChannel(IrcEvent.create(channel, sender, login, hostname));
    this.channelUsersService.removeChannelUser(getUser(ircEvent))
    TODO
    */
  }
}
