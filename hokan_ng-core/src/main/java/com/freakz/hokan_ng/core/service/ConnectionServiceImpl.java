package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.core.engine.AsyncConnector;
import com.freakz.hokan_ng.core.engine.HokanCore;
import com.freakz.hokan_ng.core.model.Connector;
import com.freakz.hokan_ng.core.model.EngineConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class ConnectionServiceImpl implements ConnectionManagerService, EngineConnector {

  @Autowired
  IrcServerConfigService ircServerConfigService;

  private Map<String, IrcServerConfig> configuredServers;

  private Map<String, HokanCore> connectedEngines = new HashMap<>();

  private List<Connector> connectors = new ArrayList<>();


  public ConnectionServiceImpl() {
    List<IrcServerConfig> servers = ircServerConfigService.getIrcServerConfigs();
    configuredServers = new HashMap<>();
    for (IrcServerConfig server : servers) {
      configuredServers.put(server.getNetwork(), server);
    }
  }

  // --- ConnectionManagerService

  @Override
  public void goOnline(String network) {

    IrcServerConfig configuredServer = configuredServers.get(network);
    Connector connector = new AsyncConnector("hokan_ng", this, configuredServer);
    this.connectors.add(connector);
    connector.connect();
  }

  @Override
  public void disconnect(String network) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void disconnectAll() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  // ---- EngineConnector

  @Override
  public void engineConnectorNickAlreadyInUse(Connector connector, IrcServerConfig configuredServer, String nickInUse) {
    this.connectors.remove(connector);
    String newNick = String.format("_%s_", nickInUse);
    Connector newConnector = new AsyncConnector(newNick, this, configuredServer);
    this.connectors.add(newConnector);
    connector.connect();
  }

  @Override
  public void engineConnectorGotOnline(Connector connector, HokanCore engine) {
    this.connectors.remove(connector);

    String network = engine.getIrcServerConfig().getNetwork();
    this.connectedEngines.put(network, engine);

    String[] channels = engine.getIrcServerConfig().getChannels();
    for (String channelToJoin : channels) {
      engine.joinChannel(channelToJoin);
    }

  }



}
