package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;

import java.util.List;

/**
 * Date: 10/31/13
 * Time: 11:40 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface IrcServerConfigService {

  List<IrcServerConfig> getIrcServerConfigs();

  IrcServerConfig createIrcServerConfig(String server, String network, int port, String password, boolean useThrottle, String channelsToJoin);

}
