package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.IrcServerConfigState;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.service.NetworkService;
import com.freakz.hokan_ng.core.engine.AsyncConnector;
import com.freakz.hokan_ng.core.engine.HokanCore;
import com.freakz.hokan_ng.core.model.Connector;
import com.freakz.hokan_ng.core.model.EngineConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 11/1/13
 * Time: 11:33 AM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Service
@Slf4j
public class ConnectionServiceImpl implements ConnectionManagerService, EngineConnector, DisposableBean {

  private static final String BOT_NICK = "hokan_ng";

  @Autowired
  private IrcServerConfigService ircServerConfigService;

  @Autowired
  private NetworkService networkService;

  @Autowired
  private ApplicationContext context;

  private Map<Network, IrcServerConfig> configuredServers;

  private Map<Network, HokanCore> connectedEngines = new HashMap<>();

  private Map<Network, Connector> connectors = new HashMap<>();

  public ConnectionServiceImpl() {
  }

  @PostConstruct
  public void initConfiguredServerMap() {
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

  private void updateServerMap() {
    List<IrcServerConfig> servers = ircServerConfigService.getIrcServerConfigs();
    configuredServers = new HashMap<>();
    for (IrcServerConfig server : servers) {
      configuredServers.put(server.getNetwork(), server);
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
  }


  // --- ConnectionManagerService

  public void abortConnectors() {
    for (Connector connector : getConnectors()) {
      log.warn("Aborting connector: " + connector);
      connector.abortConnect();
    }
  }

  @Override
  public void connect(String networkName) throws HokanException {
    Network network = networkService.getNetwork(networkName);
    connect(network);
  }

  public void connect(Network network) throws HokanException {

    HokanCore engine = getConnectedEngine(network);
    if (engine != null) {
      throw new HokanException("Engine already connected to network: " + engine);
    }

    IrcServerConfig configuredServer = configuredServers.get(network);
    if (configuredServer == null) {
      throw new HokanException("IrcServerConfig not found for network: " + network);
    }

    Connector connector;
    connector = this.connectors.get(configuredServer.getNetwork());
    if (connector == null) {
      connector = context.getBean(AsyncConnector.class);
      this.connectors.put(configuredServer.getNetwork(), connector);
      connector.connect(BOT_NICK, this, configuredServer);
    } else {
      throw new HokanException("Going online attempt already going: " + configuredServer.getNetwork());
    }

  }

  @Override
  public void disconnect(String networkName) throws HokanException {

    Network network = networkService.getNetwork(networkName);

    HokanCore engine = this.connectedEngines.get(network);
    if (engine == null) {
      throw new HokanException("No connected engine found for network: " + network);
    }
/*    IrcServerConfig config = engine.getIrcServerConfig();
    config.setIrcServerConfigState(IrcServerConfigState.DISCONNECTED);
    this.ircServerConfigService.updateIrcServerConfig(config);*/

    engine.disconnect();
    this.connectedEngines.remove(engine);
    log.info("Disconnected engine: " + engine);
  }

  @Override
  public void disconnectAll() {
    String msg = "";
    for (HokanCore engine : this.connectedEngines.values()) {
      msg += engine.toString();
      msg += "\n";
/*      IrcServerConfig config = engine.getIrcServerConfig();
      config.setIrcServerConfigState(IrcServerConfigState.DISCONNECTED);
      this.ircServerConfigService.updateIrcServerConfig(config);*/
      engine.disconnect();
    }
    this.connectedEngines.clear();
    log.info("Disconnected all engines\n" + msg);
  }

  public HokanCore getConnectedEngine(Network network) {
    return this.connectedEngines.get(network);
  }

  @Override
  public Collection<Connector> getConnectors() {
    return this.connectors.values();
  }

  // ---- EngineConnector

  @Override
  public void engineConnectorNickAlreadyInUse(Connector connector, IrcServerConfig configuredServer, String nickInUse) {
    // TODO remove?
  }

  @Override
  public void engineConnectorTooManyConnectAttempts(Connector connector, IrcServerConfig configuredServer) {
    this.connectors.remove(configuredServer.getNetwork());
    log.info("Too many connection attempts:" + connector);
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

    this.connectors.remove(network);
    this.connectedEngines.put(network, engine);
    this.networkService.updateNetwork(network);

    String[] channels = config.getChannels();
    for (String channelToJoin : channels) {
      engine.joinChannel(channelToJoin);
    }

  }

  @Override
  public void engineConnectorDisconnected(HokanCore engine) {
    IrcServerConfig config = engine.getIrcServerConfig();
//    config.setIrcServerConfigState(IrcServerConfigState.DISCONNECTED);
//    this.ircServerConfigService.updateIrcServerConfig(config);

    Network network = config.getNetwork();
    this.connectedEngines.remove(network);
    log.info("Engine disconnected: " + engine);
  }

}
