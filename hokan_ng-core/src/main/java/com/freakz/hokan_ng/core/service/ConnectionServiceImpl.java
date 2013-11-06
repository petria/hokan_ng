package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.IrcServerConfigState;
import com.freakz.hokan_ng.core.engine.AsyncConnector;
import com.freakz.hokan_ng.core.engine.HokanCore;
import com.freakz.hokan_ng.core.exception.HokanException;
import com.freakz.hokan_ng.core.model.Connector;
import com.freakz.hokan_ng.core.model.EngineConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
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

  private static final String BOT_NICK = "hokan_the_bootng";

  @Autowired
  private IrcServerConfigService ircServerConfigService;

  @Autowired
  private ApplicationContext context;

  private Map<String, IrcServerConfig> configuredServers;

  private Map<String, HokanCore> connectedEngines = new HashMap<>();

  private Map<String, Connector> connectors = new HashMap<>();

  public ConnectionServiceImpl() {
  }

  @PostConstruct
  public void initConfiguredServerMap() {
    List<IrcServerConfig> servers = ircServerConfigService.getIrcServerConfigs();
    configuredServers = new HashMap<>();
    for (IrcServerConfig server : servers) {
      configuredServers.put(server.getNetwork(), server);
      if (server.getIrcServerConfigState() == IrcServerConfigState.CONNECTED) {
        try {
          connect(server.getNetwork());
        } catch (HokanException e) {
          log.error("Couldn't get engine online: " + server.getNetwork(), e);
        }
      }
    }

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
  public void connect(String network) throws HokanException {
//    initConfiguredServerMap();

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
  public void disconnect(String network) throws HokanException {
    HokanCore engine = this.connectedEngines.get(network);
    if (engine == null) {
      throw new HokanException("No connected engine found for network: " + network);
    }
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
      engine.disconnect();
    }
    this.connectedEngines.clear();
    log.info("Disconnected all engines\n" + msg);
  }

  public HokanCore getConnectedEngine(String network) {
    return this.connectedEngines.get(network);
  }

  @Override
  public Collection<Connector> getConnectors() {
    return this.connectors.values();
  }

  // ---- EngineConnector

  @Override
  public void engineConnectorNickAlreadyInUse(Connector connector, IrcServerConfig configuredServer, String nickInUse) {
/*    this.connectors.remove(configuredServer.);
    String newNick = String.format("_%s_", nickInUse);
    Connector newConnector = context.getBean(AsyncConnector.class);
    this.connectors.add(newConnector);
    newConnector.connect(newNick, this, configuredServer);*/
  }

  @Override
  public void engineConnectorTooManyConnectAttempts(Connector connector, IrcServerConfig configuredServer) {
    this.connectors.remove(configuredServer.getNetwork());
    log.info("Too many connection attempts:" + connector);
  }

  @Override
  public void engineConnectorGotOnline(Connector connector, HokanCore engine) {

    IrcServerConfig config = engine.getIrcServerConfig();
    config.setIrcServerConfigState(IrcServerConfigState.CONNECTED);
    this.ircServerConfigService.updateIrcServerConfig(config);
    engine.setIrcServerConfig(config);

    String network = config.getNetwork();
    this.connectors.remove(network);
    this.connectedEngines.put(network, engine);

    String[] channels = config.getChannels();
    for (String channelToJoin : channels) {
      engine.joinChannel(channelToJoin);
    }

  }

  @Override
  public void engineConnectorDisconnected(HokanCore engine) {
    IrcServerConfig config = engine.getIrcServerConfig();
    config.setIrcServerConfigState(IrcServerConfigState.DISCONNECTED);
    this.ircServerConfigService.updateIrcServerConfig(config);

    String network = config.getNetwork();
    this.connectedEngines.remove(network);
    log.info("Engine disconnected: " + engine);
  }

}
