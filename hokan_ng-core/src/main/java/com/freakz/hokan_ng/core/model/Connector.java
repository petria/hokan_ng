package com.freakz.hokan_ng.core.model;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;

/**
 * User: petria
 * Date: 11/4/13
 * Time: 7:14 PM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public interface Connector {

  void connect(String nick, EngineConnector engineConnector, IrcServerConfig configuredServer);

  void abortConnect();

}
