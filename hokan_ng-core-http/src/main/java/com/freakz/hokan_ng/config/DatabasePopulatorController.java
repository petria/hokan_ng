package com.freakz.hokan_ng.config;

import com.freakz.hokan_ng.common.entity.IrcServer;
import com.freakz.hokan_ng.core.service.IrcServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Date: 10/31/13
 * Time: 11:38 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Controller
public class DatabasePopulatorController {

	@Autowired
	IrcServerService ircServerService;

	@RequestMapping(value = "/hokan_ng/populateDatabase")
	public
	@ResponseBody
	String populateDatabase() throws Exception {
		List<IrcServer> servers = ircServerService.getIrcServers();
		if (servers.size() == 0) {
			IrcServer ircServer = ircServerService.createIrcServer("localhost", 6669, "1234", false, "#HokanDEV,#HokanDEV2");
			return ircServer.toString();
		}
		return "Servers already populated!";
	}

}
