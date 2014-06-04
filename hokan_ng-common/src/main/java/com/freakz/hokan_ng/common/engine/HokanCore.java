package com.freakz.hokan_ng.common.engine;

import com.freakz.hokan_ng.common.entity.*;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import com.freakz.hokan_ng.common.rest.*;
import com.freakz.hokan_ng.common.service.*;
import com.freakz.hokan_ng.common.service.Properties;
import com.freakz.hokan_ng.common.util.CommandArgs;
import com.freakz.hokan_ng.common.util.IRCUtility;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Date: 3.6.2013
 * Time: 9:15
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class HokanCore extends PircBot implements EngineEventHandler {

  @Autowired
  protected SearchReplaceService searchReplaceService;
  @Autowired
  private ApplicationContext context;
  @Autowired
  private AccessControlService accessControlService;
  @Autowired
  private ChannelService channelService;
  @Autowired
  private JoinedUsersService joinedUsersService;
  @Autowired
  private Properties properties;
  @Autowired
  private NetworkService networkService;
  @Autowired
  private UrlLoggerService urlLoggerService;

  @Autowired
  private UserChannelService userChannelService;

  @Autowired
  private UserService userService;

  //--------

  private IrcServerConfig ircServerConfig;

  private EngineConnector engineConnector;

  private EngineCommunicator engineCommunicator;

  private Map<String, String> serverProperties = new HashMap<>();
  private Map<String, Method> methodMap = null;
  private Map<String, List<String>> whoQueries = new HashMap<>();
  private OutputQueue outputQueue;

  public HokanCore() {
    buildMethodMap();
  }

  private void buildMethodMap() {
    Class clazz = HokanCore.class;
    Method[] methods = clazz.getMethods();
    this.methodMap = new HashMap<>();
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

  private Method getEngineMethod(String name) { //}, int args) {
    List<Method> matches = new ArrayList<>();
    for (Method method : methodMap.values()) {
      if (method.getName().equals(name)) { // && method.getGenericParameterTypes().length == args) {
        matches.add(method);
      }
    }
    if (matches.size() == 1) {
      return matches.get(0);
    } else if (matches.size() > 1) {
      log.info("ffufu"); // TODO
      return matches.get(0);
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

  public void log(String message) {
    if (!message.contains("PING") && !message.contains("PONG")) {
      log.info(message);
    }
  }

  // ---

  @Override
  public synchronized void dispose() {
    outputQueue.stop();
    List<Runnable> runnableList = executor.shutdownNow();
    log.info("Runnables size: {}", runnableList.size());
    super.dispose();
  }

  public Network getNetwork() {
    return networkService.getNetwork(getIrcServerConfig().getNetwork().getName());
  }

  public Channel getChannel(String channelName) {
    Channel channel = channelService.findChannelByName(getNetwork(), channelName);
    if (channel == null) {
      channel = channelService.createChannel(getNetwork(), channelName);
    }
    return channel;
  }

  public Channel getChannel(IrcEvent ircEvent) {
    return getChannel(ircEvent.getChannel());
  }

  public User getUser(IrcEvent ircEvent) {
    try {
      User user;
      User maskUser = this.userService.getUserByMask(ircEvent.getMask());
      if (maskUser != null) {
        user = maskUser;
      } else {
        user = this.userService.findUser(ircEvent.getSender());
        if (user == null) {
          user = new User(ircEvent.getSender());
          user = userService.updateUser(user);
        }

      }
      user.setRealMask(StringStuff.quoteRegExp(ircEvent.getMask()));
      this.userService.updateUser(user);
      return user;
    } catch (HokanException e) {
      coreExceptionHandler(e);
    }
    return null;
  }

  public Map<String, String> getServerProperties() {
    return serverProperties;
  }

  // --- PircBot

  @Override
  protected void onDisconnect() {
    this.engineConnector.engineConnectorDisconnected(this);
  }

  @Override
  protected void onUnknown(String line) {
    log.info("UNKNOWN: {}", line);
    if (line.contains("Ping timeout")) {
      this.engineConnector.engineConnectorPingTimeout(this);
    } else if (line.toLowerCase().contains("excess flood")) {
      this.engineConnector.engineConnectorExcessFlood(this);
    }
  }

  public void sendWhoQuery(String channel) {
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
    this.joinedUsersService.clearJoinedUsers(channel);
    for (String whoLine : whoReplies) {
      String[] split = whoLine.split(" ");
      String nick = split[5];
      String mask = split[5] + "!" + split[2] + "@" + split[3];
      String userModes = split[6];
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
      this.joinedUsersService.createJoinedUser(channel, user, userModes);
    }
  }

  @Override
  protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    sendWhoQuery(channel);
  }

  @Override
  protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    sendWhoQuery(channel);
  }

  @Override
  protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    sendWhoQuery(channel);
  }

  @Override
  protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    sendWhoQuery(channel);
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

  private void coreExceptionHandler(Exception e) {
    log.error("---------------------------");
    log.error("Exception", e);
    log.error("---------------------------");
    try {
      List<User> masterUsers = accessControlService.getMasterUsers();
      String message = "Exception caught: " + e;
/*      for (User user : masterUsers) {
        sendMessage(user.getNick(), message);
      }*/
    } catch (HokanException e1) {
      e1.printStackTrace();  //ToDO epic fail?
    }
  }

  private boolean isBotOp(Channel channel) {
    for (JoinedUser user : joinedUsersService.findJoinedUsers(channel)) {
      if (user.getUser().getNick().equalsIgnoreCase(getName())) {
        return user.isOp();
      }
    }
    return false;
  }

  @Override
  protected void onMessage(String channel, String sender, String login, String hostname, String message) {
    String toMe = getName() + ": ";
    boolean isToMe = false;
    if (message.startsWith(toMe)) {
      message = message.replaceFirst(toMe, "");
      isToMe = true;
    }
    IrcMessageEvent ircEvent = (IrcMessageEvent) IrcEventFactory.createIrcMessageEvent(getName(), getNetwork().getName(), channel, sender, login, hostname, message);
    ircEvent.setToMe(isToMe);

    Network nw = getNetwork();
    nw.addToLinesReceived(1);
    this.networkService.updateNetwork(nw);

    User user = getUser(ircEvent);

    Channel ch = getChannel(ircEvent);
    ch.addToLinesReceived(1);
    ch.setLastActive(new Date());

    ircEvent.setBotOp(isBotOp(ch));

    String lastWriter = ch.getLastWriter();
    if (lastWriter != null && lastWriter.equalsIgnoreCase(sender)) {
      int spree = ch.getLastWriterSpree();
      spree++;
      ch.setLastWriterSpree(spree);
      if (spree > ch.getWriterSpreeRecord()) {
        ch.setWriterSpreeRecord(spree);
        ch.setWriterSpreeOwner(sender);
      }
    } else {
      ch.setLastWriterSpree(1);
    }
    ch.setLastWriter(sender);

    UserChannel userChannel = userChannelService.getUserChannel(user, ch);
    if (userChannel == null) {
        userChannel = new UserChannel(user, ch);
    }
    userChannel.setLastMessage(message);
    userChannel.setLastMessageTime(new Date());
    try {
      userChannelService.storeUserChannel(userChannel);
    } catch (HokanException e) {
      coreExceptionHandler(e);
    }

    boolean wlt = properties.getChannelPropertyAsBoolean(ch, PropertyName.PROP_CHANNEL_DO_WHOLELINE_TRICKERS, false);
    if (wlt || ircEvent.isToMe()) {
      WholeLineTrickers wholeLineTrickers = new WholeLineTrickers(this);
      wholeLineTrickers.checkWholeLineTrickers(ircEvent);
    }
    urlLoggerService.catchUrls(ircEvent, ch, this);

    if (accessControlService.isMasterUser(ircEvent)) {
      handleBuiltInCommands(ircEvent);
    }


    EngineRequest request = new EngineRequest(ircEvent);
    this.engineCommunicator.sendEngineMessage(request, this);
    this.channelService.updateChannel(ch);
  }

  private void handleBuiltInCommands(IrcMessageEvent ircEvent) {
    String message = ircEvent.getMessage();
    CommandArgs args = new CommandArgs(ircEvent.getMessage());
    if (message.startsWith("!qset ")) {

      boolean ok = outputQueue.setQueueValues(args.getArgs());
      handleSendMessage(ircEvent.getChannel(), "!qset: " + ok);
      String info = String.format("Throttle[%s]: sleepTime %d - maxLines - %d - fullLineLength %d - fullLineSleepTime %d - throttleBaseSleepTime %d",
          outputQueue.isUsingThrottle() + "",
          outputQueue.defSleepTime, outputQueue.defMaxLines,
          outputQueue.defFullLineLength, outputQueue.defFullLineSleepTime,
          outputQueue.defThrottleBaseSleepTime);

      handleSendMessage(ircEvent.getChannel(), info);


    } else if (message.equals("!qclear")) {

      outputQueue.clearOutQueue();
      handleSendMessage(ircEvent.getChannel(), "OutQueue cleared!");

    } else if (message.startsWith("!qthrottle")) {

      outputQueue.setThrottle(StringStuff.parseBooleanString(args.getArgs()));
      handleSendMessage(ircEvent.getChannel(), String.format("Throttle[%s]", outputQueue.isUsingThrottle() + ""));

    } else if (message.equals("!qinfo")) {
      String info = String.format("Throttle[%s]: sleepTime %d - maxLines - %d - fullLineLength %d - fullLineSleepTime %d - throttleBaseSleepTime %d",
          outputQueue.isUsingThrottle() + "",
          outputQueue.defSleepTime, outputQueue.defMaxLines,
          outputQueue.defFullLineLength, outputQueue.defFullLineSleepTime,
          outputQueue.defThrottleBaseSleepTime);

      handleSendMessage(ircEvent.getChannel(), info);

    } else if (message.equals("!methodmap")) {
      log.info("Re-building method map!");
      buildMethodMap();
    } else if (message.equals("!elist")) {
      int size = engineCommunicator.getEngineHandlers().size();
      for (RestUrl restUrl : engineCommunicator.getEngineHandlers().values()) {
        handleSendMessage(ircEvent.getChannel(), restUrl.toString());
      }

    }
  }

  @Override
  protected void onPrivateMessage(String sender, String login, String hostname, String message) {
    IrcMessageEvent ircEvent = (IrcMessageEvent) IrcEventFactory.createIrcMessageEvent(getName(), getNetwork().getName(), sender, sender, login, hostname, message);
    ircEvent.setPrivate(true);

    Network nw = getNetwork();
    nw.addToLinesReceived(1);
    this.networkService.updateNetwork(nw);


    EngineRequest request = new EngineRequest(ircEvent);
    this.engineCommunicator.sendEngineMessage(request, this);
    log.info("Message: {}", ircEvent.getMessage());
  }

  @Override
  public void handleEngineError(EngineResponse response) {
    log.error("Engine request failed: {}", response);
    coreExceptionHandler(new HokanException(response.getException()));
  }

  @Override
  @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
  public void handleEngineResponse(EngineResponse response) {

    if (response.getException() != null) {
      String error = " failed: " + response.getException();
      String message;
      String target;
      if (response.getRequest().getIrcEvent() instanceof IrcMessageEvent) {
        message = response.getRequest().getIrcEvent().getSender() + ": " + error;
        target = response.getRequest().getIrcEvent().getChannel();
      } else {
        message = error;
        target = response.getRequest().getIrcEvent().getSender();
      }
      sendMessage(target, message);
      return;
    }

    handleSendMessage(response);
    if (response.getCommandClass() != null) {
      Channel ch = getChannel(response.getRequest().getIrcEvent().getChannel());
      ch.addCommandsHandled(1);
      this.channelService.updateChannel(ch);
    }

    for (EngineMethodCall methodCall : response.getEngineMethodCalls()) {
      String methodName = methodCall.getMethodName();
      String[] methodArgs = methodCall.getMethodArgs();

      log.info("Executing engine method : " + methodName);
      log.info("Engine method args      : " + StringStuff.arrayToString(methodArgs, ", "));
      Method method = getEngineMethod(methodName);
      if (method != null) {
        String[] args = new String[method.getParameterTypes().length];
        int i = 0;
        for (String arg : methodArgs) {
          args[i] = arg;
          i++;
        }
        log.info("Using method args       : " + StringStuff.arrayToString(args, ", "));
        try {
          log.info("Invoking method         : {}", method);
          Object result = method.invoke(this, (Object[]) args);
          log.info("Invoke   result         : {}", result);
        } catch (Exception e) {
          coreExceptionHandler(e);
          log.error("Couldn't do engine method!", e);
        }
      } else {
        log.error("Couldn't find method for: " + methodName);
      }

    }
  }

  public void startOutputQueue() {
    this.outputQueue = this.context.getBean(OutputQueue.class);
    this.outputQueue.init(this, getIrcServerConfig().isThrottleInUse());
  }

  protected void handleSendMessage(EngineResponse response) {
    String channel = response.getReplyTo();
    String message = response.getResponseMessage();
    if (message != null && message.trim().length() > 1) {
      if (!response.isNoSearchReplace()) {
        boolean doSr = false;
        if (!response.getRequest().getIrcEvent().isPrivate()) {
          Channel ch = getChannel(response.getRequest().getIrcEvent().getChannel());
          doSr = properties.getChannelPropertyAsBoolean(ch, PropertyName.PROP_CHANNEL_DO_SEARCH_REPLACE, false);
        }
        if (doSr) {
          message = handleSearchReplace(response, message);
        }
      }
      handleSendMessage(channel, message);
    }
  }

  private String handleSearchReplace(EngineResponse response, String message) {
    List<SearchReplace> searchReplaces = searchReplaceService.getSearchReplaces();
    for (SearchReplace searchReplace : searchReplaces) {
      message = message.replaceAll(searchReplace.getSearch(), searchReplace.getReplace());
    }
    return message;
  }

  public void handleSendMessage(String channel, String message) {
    Channel ch = null;
    if (channel.startsWith("#")) {
      ch = getChannel(channel);
    }
    Network nw = getNetwork();
    String[] lines = message.split("\n");
    for (String line : lines) {
      String[] split = IRCUtility.breakUpMessageByIRCLineLength(channel, line);
      for (String l : split) {
        String raw = "PRIVMSG " + channel + " :" + l;
        this.outputQueue.addLine(raw);
        if (ch != null) {
          ch.addToLinesSent(1);
        }
        nw.addToLinesSent(1);
      }
    }
    if (ch != null) {
      this.channelService.updateChannel(ch);
    }
    this.networkService.updateNetwork(nw);
  }

  @Override
  protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
    Date topicDate = new Date(date);
    Channel ch = getChannel(channel);
    ch.setTopic(topic);
    ch.setTopicSetBy(setBy);
    ch.setTopicSetDate(topicDate);
    ch = channelService.updateChannel(ch);
    log.info("Topic '{}' set by {}", ch.getTopic(), ch.getTopicSetBy());
  }


  @Override
  protected void onJoin(String channel, String sender, String login, String hostname) {
    IrcEvent ircEvent = IrcEventFactory.createIrcEvent(getName(), getNetwork().getName(), channel, sender, login, hostname);
    Channel ch = getChannel(ircEvent);
    UserChannel userChannel = userChannelService.getUserChannel(getUser(ircEvent), ch);

    log.info("{} joined channel: {}", sender, channel);

    if (sender.equalsIgnoreCase(getNick())) {
      Network nw = getNetwork();
      nw.addToChannelsJoined(1);
      this.networkService.updateNetwork(nw);

      ch.setChannelState(ChannelState.JOINED);
      if (ch.getFirstJoined() == null) {
        Date d = new Date();
        ch.setLastWriter(getName());
        ch.setMaxUserCount(1);
        ch.setFirstJoined(d);
        ch.setLastActive(d);
        ch.setMaxUserCountDate(d);
        ch.setWriterSpreeOwner(getName());
      }
    } else {
      boolean doJoin = properties.getChannelPropertyAsBoolean(ch, PropertyName.PROP_CHANNEL_DO_JOIN_MESSAGE, false);
      if (doJoin) {
        String message = userChannel.getJoinComment();
        if (message != null && message.length() > 0) {
          handleSendMessage(channel, sender + " -> " + message);
        }
      }

      try {
        this.joinedUsersService.createJoinedUser(ch, getUser(ircEvent), "");
      } catch (HokanServiceException e) {
        coreExceptionHandler(e);
      }
    }

    int oldC = ch.getMaxUserCount();
    int newC = getUsers(ch.getChannelName()).length;
    if (newC > oldC) {
      log.info("Got new channel users record: " + newC + " > " + oldC);
      ch.setMaxUserCount(newC);
      ch.setMaxUserCountDate(new Date());
    }

    this.channelService.updateChannel(ch);

    userChannel.setLastJoin(new Date());
    try {
      this.userChannelService.storeUserChannel(userChannel);
    } catch (HokanException e) {
      coreExceptionHandler(e);
    }

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

      IrcEvent ircEvent = IrcEventFactory.createIrcEvent(getName(), getNetwork().getName(), channel, sourceNick, sourceLogin, sourceHostname);

      UserChannel userChannel = userChannelService.getUserChannel(getUser(ircEvent), getChannel(ircEvent));
      userChannel.setLastPart(new Date());
      userChannel.setLastPartMessage(reason);
//      userChannel.getUser().setLoggedIn(0);
      try {
        this.userChannelService.storeUserChannel(userChannel);
      } catch (HokanException e) {
        coreExceptionHandler(e);
      }
    }

  }

  @Override
  protected void onPart(String channel, String sender, String login, String hostname, String message) {
    IrcEvent ircEvent = IrcEventFactory.createIrcEvent(getName(), getNetwork().getName(), channel, sender, login, hostname);
    Channel ch = getChannel(ircEvent);
    log.info("{} part channel: {}", sender, channel);
    if (sender.equalsIgnoreCase(getNick())) {
      ch.setChannelState(ChannelState.NOT_JOINED);
      try {
        this.joinedUsersService.clearJoinedUsers(ch);
      } catch (HokanServiceException e) {
        coreExceptionHandler(e);
      }
    } else {
      try {
        this.joinedUsersService.removeJoinedUser(ch, getUser(ircEvent));
      } catch (HokanServiceException e) {
        coreExceptionHandler(e);
      }
    }
    this.channelService.updateChannel(ch);
    UserChannel userChannel = userChannelService.getUserChannel(getUser(ircEvent), ch);
    userChannel.setLastPart(new Date());
    userChannel.setLastPartMessage(message);
    try {
      this.userChannelService.storeUserChannel(userChannel);
    } catch (HokanException e) {
      coreExceptionHandler(e);
    }

  }

  @Override
  protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
    for (String joined : getChannels()) {
      sendWhoQuery(joined);
    }
  }

  public String toString() {
    String config = ircServerConfig.toString();
    String props = getServerProperties().toString();
    return String.format("%s: %s", config, props);
  }

}
