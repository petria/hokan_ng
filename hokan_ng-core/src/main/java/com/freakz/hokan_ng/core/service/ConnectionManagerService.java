package com.freakz.hokan_ng.core.service;

import com.freakz.hokan_ng.core.exception.HokanException;
import com.freakz.hokan_ng.core.model.Connector;

import java.util.Collection;

/**
 * Date: 3.6.2013
 * Time: 10:39
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public interface ConnectionManagerService {

  void connect(String network) throws HokanException;

  void disconnect(String network) throws HokanException;

  void disconnectAll();

  Collection<Connector> getConnectors();

}
