package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.commmon.rest.EngineResponse;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 11:18 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface EngineCommunicator {

  EngineResponse sendEngineMessage(String request);

}
