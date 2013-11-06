package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.commmon.rest.EngineRequest;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 11:18 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface EngineCommunicator {

  void sendEngineMessage(EngineRequest request, EngineEventHandler engineEventHandler);

}