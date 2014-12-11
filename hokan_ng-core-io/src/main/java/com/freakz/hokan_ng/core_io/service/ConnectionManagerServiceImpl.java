package com.freakz.hokan_ng.core_io.service;

import com.freakz.hokan_ng.common.engine.AsyncConnector;
import com.freakz.hokan_ng.common.engine.Connector;
import com.freakz.hokan_ng.common.engine.EngineConnector;
import com.freakz.hokan_ng.common.engine.HokanCore;
import com.freakz.hokan_ng.common.entity.*;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import com.freakz.hokan_ng.common.jms.HokanTopicPublisher;
import com.freakz.hokan_ng.common.rest.messages.CoreRequest;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.freakz.hokan_ng.common.service.NetworkService;
import com.freakz.hokan_ng.common.service.PropertyService;
import com.freakz.hokan_ng.common.service.UserService;
import com.freakz.hokan_ng.core_io.jms.CoreIOTopicListener;
import com.freakz.hokan_ng.core_io.jms.CoreIOTopicPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Date: 11/1/13
 * Time: 11:33 AM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Service
@Slf4j
public class ConnectionManagerServiceImpl
    implements ConnectionManagerService, EngineConnector, DisposableBean {

  @Autowired
  private ChannelService channelService;

  @Autowired
  private ApplicationContext context;

  @Autowired
  private IrcServerConfigService ircServerConfigService;

  @Autowired
  private NetworkService networkService;

  @Autowired
  private PropertyService propertyService;

  @Autowired
  private UserService userService;

  @Autowired
  private CoreIOTopicPublisher topicPublisher;

  //----------

  private Map<String, IrcServerConfig> configuredServers;

  private Map<String, HokanCore> connectedEngines = new HashMap<>();

  private Map<String, Connector> connectors = new HashMap<>();
  private String botNick;

  public ConnectionManagerServiceImpl() {
  }


  @PostConstruct
  public void postInit() throws HokanException {

    propertyService.setProperty(PropertyName.PROP_SYS_CORE_IO_UPTIME, "" + new Date().getTime());
    userService.resetLoggedInUsers();
    userService.resetOlpos();
    updateServerMap();
    for (IrcServerConfig server : this.configuredServers.values()) {
      if (server.getIrcServerConfigState() == IrcServerConfigState.CONNECTED) {
        try {
          connect(server.getNetwork());
        } catch (HokanException e) {
          log.error("Couldn't get engine online: " + server.getNetwork(), e);
        }
      }
    }
  }

  private boolean botNickOk() {
    try {
      this.botNick = propertyService.findProperty(PropertyName.PROP_SYS_BOT_NICK).getValue();
    } catch (Exception e) {
      log.error("Error occured {}", e);
      return false;
    }
    return botNick != null && botNick.length() > 0;
  }

  private void updateServerMap() {
    List<IrcServerConfig> servers = ircServerConfigService.getIrcServerConfigs();
    configuredServers = new HashMap<>();
    for (IrcServerConfig server : servers) {
      configuredServers.put(server.getNetwork().getName(), server);
    }
  }

  @Override
  public void updateServers() {
    updateServerMap();
  }

  @Override
  public void destroy() throws Exception {
    log.warn("Going to be destroyed!");
    abortConnectors();
    disconnectAll();
    Thread.sleep(1 * 1000);
    stopEngines();
    Thread.sleep(1 * 1000);
    log.warn("Destroy phase done!");
  }


  public void stopEngines() {
    for (HokanCore core : this.connectedEngines.values()) {
      try {
        core.dispose();
      } catch (Exception e) {
        log.error("Error stopping engines: {}", e);
      }
    }
  }

  // --- ConnectionManagerService

  public void abortConnectors() {
    for (Connector connector : getConnectors()) {
      log.warn("Aborting connector: " + connector);
      connector.abortConnect();
    }
  }

  @Override
  public void joinChannels(String networkName) throws HokanServiceException {
    Network network;
    try {
      network = networkService.getNetwork(networkName);
      HokanCore engine = this.connectedEngines.get(networkName);
      if (engine == null) {
        throw new HokanServiceException("Engine not online: " + networkName);
      }
      joinChannels(engine, network);

    } catch (HokanException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void connect(String networkName) throws HokanServiceException {
    Network network;
    try {
      network = networkService.getNetwork(networkName);
      connect(network);
    } catch (HokanException e) {
      throw new HokanServiceException(e);
    }
  }

  public void connect(Network network) throws HokanServiceException {

    if (!botNickOk()) {
      throw new HokanServiceException("PropertyName.PROP_SYS_BOT_NICK not configured correctly: " + this.botNick);
    }
    updateServerMap();
    HokanCore engine = getConnectedEngine(network);
    if (engine != null) {
      throw new HokanServiceException("Engine already connected to network: " + engine);
    }

    IrcServerConfig configuredServer = configuredServers.get(network.getName());
    if (configuredServer == null) {
      throw new HokanServiceException("IrcServerConfig not found for network: " + network);
    }
    configuredServer.setIrcServerConfigState(IrcServerConfigState.CONNECTED);
    this.ircServerConfigService.updateIrcServerConfig(configuredServer);

    Connector connector;
    connector = this.connectors.get(configuredServer.getNetwork().getName());
    if (connector == null) {
      connector = context.getBean(AsyncConnector.class);
      this.connectors.put(configuredServer.getNetwork().getName(), connector);
      connector.connect(this.botNick, this, configuredServer);
    } else {
      throw new HokanServiceException("Going online attempt already going: " + configuredServer.getNetwork());
    }

  }

  @Override
  public void disconnect(String networkName) throws HokanServiceException {
    Network network;

    network = networkService.getNetwork(networkName);
    if (network == null) {
      throw new HokanServiceException("Unknown Network name: " + networkName);
    }

    HokanCore engine = this.connectedEngines.get(network.getName());
    if (engine == null) {
      throw new HokanServiceException("No connected Engine found for Network: " + network);
    }
    engine.getIrcServerConfig().setIrcServerConfigState(IrcServerConfigState.DISCONNECTED);
    this.ircServerConfigService.updateIrcServerConfig(engine.getIrcServerConfig());
    engine.disconnect();
    this.connectedEngines.remove(networkName);
    log.info("Disconnected engine: " + engine);
  }

  @Override
  public void disconnectAll() {
    String msg = "";
    for (HokanCore engine : this.connectedEngines.values()) {
      msg += engine.toString();
      msg += "\n";
      engine.disconnect();
      engine.dispose();
    }
    this.connectedEngines.clear();
    log.info("Disconnected all engines\n" + msg);
  }

  // -------

  public HokanCore getConnectedEngine(Network network) {
    return this.connectedEngines.get(network.getName());
  }

  @Override
  public Collection<Connector> getConnectors() {
    return this.connectors.values();
  }


  // ---- EngineConnector


  @Override
  public void engineConnectorTooManyConnectAttempts(Connector connector, IrcServerConfig configuredServer) {
    this.connectors.remove(configuredServer.getNetwork().getName());
    log.info("Too many connection attempts:" + connector);
  }

  @Override
  public void engineConnectorTooManyConnections(Connector connector, IrcServerConfig configuredServer) {
    log.info("Too many connections:" + connector);
  }

  @Override
  public void engineConnectorGotOnline(Connector connector, HokanCore engine) throws HokanException {

    IrcServerConfig config = engine.getIrcServerConfig();
    config.setIrcServerConfigState(IrcServerConfigState.CONNECTED);
    this.ircServerConfigService.updateIrcServerConfig(config);
    engine.setIrcServerConfig(config);

    Network network = config.getNetwork();
    if (network.getFirstConnected() == null) {
      network.setFirstConnected(new Date());
    }
    network.addToConnectCount(1);

    CoreIOTopicListener topicListener = context.getBean(CoreIOTopicListener.class);
    topicListener.setEngineEventHandler(engine);
    engine.setTopicListener(topicListener);
    engine.setTopicPublisher(this.topicPublisher);

    engine.startOutputQueue();

    this.connectors.remove(network.getName());
    this.connectedEngines.put(network.getName(), engine);
    this.networkService.updateNetwork(network);

    joinChannels(engine, network);

  }

  private void joinChannels(HokanCore engine, Network network) {
    List<Channel> channels = this.channelService.findChannels(network, ChannelState.JOINED);
    for (Channel channelToJoin : channels) {
      engine.joinChannel(channelToJoin.getChannelName());
    }
  }

  @Override
  public void engineConnectorDisconnected(HokanCore engine) {
    IrcServerConfig config = engine.getIrcServerConfig();
    Network network = config.getNetwork();
    this.connectedEngines.remove(network.getName());
    log.info("Engine disconnected: " + engine);
  }

  @Override
  public void engineConnectorPingTimeout(HokanCore hokanCore) {
    log.info("Engine ping timeout: {}", hokanCore);
    try {
      this.connectedEngines.remove(hokanCore.getIrcServerConfig().getNetwork().getName());
      connect(hokanCore.getIrcServerConfig().getNetwork().getName());
    } catch (HokanServiceException e) {
      log.error("Couldn't re-connect after ping timeout!", e);
    }
  }

  @Override
  public void engineConnectorExcessFlood(HokanCore hokanCore) {
    log.info("Engine Excess Flood: {}", hokanCore);
    try {
      this.connectedEngines.remove(hokanCore.getIrcServerConfig().getNetwork().getName());
      connect(hokanCore.getIrcServerConfig().getNetwork().getName());
    } catch (HokanServiceException e) {
      log.error("Couldn't re-connect after Excess Flood!", e);
    }

  }

  @Scheduled(fixedDelay = 30000)
  public void updateRuntime() throws Exception {
    Property property = propertyService.findProperty(PropertyName.PROP_SYS_CORE_IO_RUNTIME);
    if (property == null) {
      property = new Property(PropertyName.PROP_SYS_CORE_IO_RUNTIME, "0", "");
    } else {
      if (property.getValue() == null) {
        property.setValue("0");
      } else {
        int value = Integer.parseInt(property.getValue());
        value += 60;
        property.setValue(value + "");
      }
    }
    propertyService.saveProperty(property);
  }

  @Override
  public void handleCoreRequest(CoreRequest request) {
    try {
      Channel target = channelService.findChannelById(request.getTargetChannelId());
      HokanCore core = this.connectedEngines.get(target.getNetwork().getName());
      core.sendMessage(target.getChannelName(), request.getMessage());
    } catch (HokanDAOException e) {
      log.error("CoreRequest error!", e);
    }
  }

}
