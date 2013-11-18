package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.common.engine.Connector;
import com.freakz.hokan_ng.common.exception.HokanServiceException;

import java.util.Collection;

/**
 * Date: 3.6.2013
 * Time: 10:39
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public interface ConnectionManagerService {

  void joinChannels(String network) throws HokanServiceException;

  void connect(String network) throws HokanServiceException;

  void disconnect(String network) throws HokanServiceException;

  void disconnectAll();

  Collection<Connector> getConnectors();

  void updateServers();
}
