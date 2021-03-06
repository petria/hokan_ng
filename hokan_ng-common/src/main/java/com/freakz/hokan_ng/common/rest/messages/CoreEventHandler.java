package com.freakz.hokan_ng.common.rest.messages;

/**
 * User: petria
 * Date: 12/12/13
 * Time: 2:18 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface CoreEventHandler {

  void handleCoreResponse(CoreResponse response);

  void handleCoreError(CoreResponse response);

}
