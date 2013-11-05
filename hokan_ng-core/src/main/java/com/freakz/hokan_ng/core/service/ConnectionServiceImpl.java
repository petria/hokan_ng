package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.core.engine.AsyncConnector;
import com.freakz.hokan_ng.core.engine.HokanCore;
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
  IrcServerConfigService ircServerService;

  Map<String, IrcServerConfig> configuredServers;

  List<HokanCore> connectedEngines = new ArrayList<>();

  List<AsyncConnector> connectors = new ArrayList<>();


  public ConnectionServiceImpl() {
    List<IrcServerConfig> servers = ircServerService.getIrcServerConfigs();
    configuredServers = new HashMap<>();
    for (IrcServerConfig server : servers) {
      configuredServers.put(server.getNetwork(), server);
    }
  }


  @Override
  public void goOnline(String network) {
    IrcServerConfig configuredServer = configuredServers.get(network);
    AsyncConnector connector = new AsyncConnector(configuredServer);
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

  @Override
  public void engineConnected(HokanCore engine) {
    this.connectedEngines.add(engine);
  }

  @Override
  public void engineDisconnected(HokanCore engine) {
    this.connectedEngines.remove(engine);
  }

  @Override
  public List<HokanCore> getConnectedEngines() {
    return this.connectedEngines;
  }
}
