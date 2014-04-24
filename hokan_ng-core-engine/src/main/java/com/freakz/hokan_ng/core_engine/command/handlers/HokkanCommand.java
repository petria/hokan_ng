package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 2:49 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface HokkanCommand {

  String getMatchPattern();

  String getName();

  void handleLine(InternalRequest request, EngineResponse response) throws Exception;

}
