package com.freakz.hokan_ng.core_io.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.IrcServerConfigState;
import com.freakz.hokan_ng.common.exception.HokanException;

import java.util.List;

/**
 * Date: 10/31/13
 * Time: 11:40 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface IrcServerConfigService {

  List<IrcServerConfig> getIrcServerConfigs();

  IrcServerConfig createIrcServerConfig(String network, String server, int port, String password, boolean useThrottle, IrcServerConfigState disconnected) throws HokanException;

  IrcServerConfig updateIrcServerConfig(IrcServerConfig ircServerConfig);

}
