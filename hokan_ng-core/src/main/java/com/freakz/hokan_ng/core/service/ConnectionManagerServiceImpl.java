package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServer;
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
	IrcServerService ircServerService;

	Map<String, IrcServer> configuredServers;

	public ConnectionManagerServiceImpl() {
		List<IrcServer> servers = ircServerService.getIrcServers();
		configuredServers = new HashMap<>();
		for (IrcServer server : servers) {
			configuredServers.put(server.getNetwork(), server);
		}
	}


	@Override
	public void goOnline(String network) {
		IrcServer server = configuredServers.get(network);
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
