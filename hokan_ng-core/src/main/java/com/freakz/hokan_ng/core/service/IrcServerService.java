package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServer;

import java.util.List;

/**
 * Date: 10/31/13
 * Time: 11:40 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface IrcServerService {

	List<IrcServer> getIrcServers();

	IrcServer createIrcServer(String server, int port, String password, boolean useThrottle, String channelsToJoin);

}
