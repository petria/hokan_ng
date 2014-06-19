package com.freakz.hokan_ng.common.engine;

import com.freakz.hokan_ng.common.entity.RestUrl;
import com.freakz.hokan_ng.common.rest.EngineRequest;

import java.util.Map;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 11:18 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface EngineCommunicator {

  void sendEngineMessage(EngineRequest request, EngineEventHandler engineEventHandler);

  Map<String, RestUrl> getEngineHandlers();

}
