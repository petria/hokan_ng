package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.IrcServerConfigState;
import com.freakz.hokan_ng.core.dao.IrcServerConfigDAO;
import com.freakz.hokan_ng.core.exception.HokanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public IrcServerConfig createIrcServerConfig(String network,
                                               String server,
                                               int port,
                                               String password,
                                               boolean useThrottle,
                                               String channelsToJoin,
                                               IrcServerConfigState state) {
    try {
      return ircServerConfigDAO.createIrcServerConfig(network, server, port, password, useThrottle, channelsToJoin, state);
    } catch (HokanException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  @Transactional
  public IrcServerConfig updateIrcServerConfig(IrcServerConfig ircServerConfig) {
    try {
      return ircServerConfigDAO.updateIrcServerConfig(ircServerConfig);
    } catch (HokanException e) {
      e.printStackTrace();
    }
    return null;
  }


}