package com.freakz.hokan_ng.core.model;


import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.core.engine.HokanCore;

/**
 * User: petria
 * Date: 11/4/13
 * Time: 7:17 PM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public interface EngineConnector {

  void engineConnectorNickAlreadyInUse(Connector connector, IrcServerConfig configuredServer, String nickInUse);

  void engineConnectorGotOnline(Connector connector, HokanCore engine) throws HokanException;

  void engineConnectorTooManyConnectAttempts(Connector connector, IrcServerConfig configuredServer);

  void engineConnectorDisconnected(HokanCore engine);

}
