package com.freakz.hokan_ng.core_io.service;

import com.freakz.hokan_ng.common.dao.IrcServerConfigDAO;
import com.freakz.hokan_ng.common.dao.NetworkDAO;
import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.IrcServerConfigState;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Date: 10/31/13
 * Time: 11:48 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class IrcServerConfigServiceImpl implements IrcServerConfigService {

  @Autowired
  IrcServerConfigDAO ircServerConfigDAO;

  @Autowired
  NetworkDAO networkDAO;

  public IrcServerConfigServiceImpl() {
  }


  @Override
  public List<IrcServerConfig> getIrcServerConfigs() {
    try {
      return ircServerConfigDAO.getIrcServerConfigs();
    } catch (HokanException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public IrcServerConfig createIrcServerConfig(String networkName,
                                               String server,
                                               int port,
                                               String password,
                                               boolean useThrottle,
                                               IrcServerConfigState state) throws HokanException {

    Network network = null;
    try {
      network = networkDAO.getNetwork(networkName);
    } catch (HokanException e) {
      // ignore
    }
    if (network == null) {
      network = networkDAO.createNetwork(networkName);
    }

    try {
      return ircServerConfigDAO.createIrcServerConfig(network, server, port, password, useThrottle, state);
    } catch (HokanException e) {
      throw new HokanException(e.getMessage());
    }
  }

  @Override
  public IrcServerConfig updateIrcServerConfig(IrcServerConfig ircServerConfig) {
    try {
      return ircServerConfigDAO.updateIrcServerConfig(ircServerConfig);
    } catch (HokanException e) {
      e.printStackTrace();
    }
    return null;
  }


}
