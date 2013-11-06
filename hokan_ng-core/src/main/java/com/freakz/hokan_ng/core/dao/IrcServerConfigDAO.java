package com.freakz.hokan_ng.core.dao;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.entity.IrcServerConfigState;
import com.freakz.hokan_ng.core.exception.HokanException;

import java.util.List;

/**
 * Date: 3.6.2013
 * Time: 11:35
 *
 * @author Petri Airio (petri.airio@siili.fi)
 */
public interface IrcServerConfigDAO {

  List<IrcServerConfig> getIrcServerConfigs() throws HokanException;

  IrcServerConfig createIrcServerConfig(String network, String server, int port, String password, boolean useThrottle, String channelsToJoin, IrcServerConfigState state) throws HokanException;

  IrcServerConfig updateIrcServerConfig(IrcServerConfig ircServerConfig) throws HokanException;

}
