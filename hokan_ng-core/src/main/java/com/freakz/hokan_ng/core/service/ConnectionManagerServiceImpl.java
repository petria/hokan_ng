package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ConnectionManagerServiceImpl implements ConnectionManagerService {

  @Autowired
  IrcServerConfigService ircServerService;

  Map<String, IrcServerConfig> configuredServers;

  public ConnectionManagerServiceImpl() {
    List<IrcServerConfig> servers = ircServerService.getIrcServerConfigs();
    configuredServers = new HashMap<>();
    for (IrcServerConfig server : servers) {
      configuredServers.put(server.getNetwork(), server);
    }
  }


  @Override
  public void goOnline(String network) {
    IrcServerConfig server = configuredServers.get(network);
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void disconnect(String network) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void disconnectAll() {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
