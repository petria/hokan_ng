package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.entity.IrcServer;
import com.freakz.hokan_ng.core.dao.IrcServerDAO;
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
public class IrcServerImpl implements IrcServerService {

	@Autowired
	IrcServerDAO ircServerDAO;

	public IrcServerImpl() {
	}


	@Override
	public List<IrcServer> getIrcServers() {
		try {
			return ircServerDAO.getIrcServers();
		} catch (HokanException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public IrcServer createIrcServer(String server, int port, String password, boolean useThrottle, String channelsToJoin) {
		try {
			return ircServerDAO.createIrcServer(server, port, password, useThrottle, channelsToJoin);
		} catch (HokanException e) {
			e.printStackTrace();
		}
		return null;
	}
}
