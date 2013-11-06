package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.common.rest.EngineResponse;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 1:28 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface EngineEventHandler {

  void handleEngineResponse(EngineResponse response);

}
