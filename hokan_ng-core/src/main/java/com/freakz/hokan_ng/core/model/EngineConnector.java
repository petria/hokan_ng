package com.freakz.hokan_ng.core.model;

import com.freakz.hokan_ng.core.engine.HokanCore;

import java.util.List;

/**
 * User: petria
 * Date: 11/4/13
 * Time: 7:17 PM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public interface EngineConnector {

  void engineConnected(HokanCore engine);

  void engineDisconnected(HokanCore engine);

  List<HokanCore> getConnectedEngines();

}
