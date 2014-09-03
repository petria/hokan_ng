package com.freakz.hokan_ng.common.rest.messages;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 1:28 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface EngineEventHandler {

  void handleEngineResponse(EngineResponse response);

  void handleEngineError(EngineResponse response);

}
