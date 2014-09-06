package com.freakz.hokan_ng.common.rest.messages.router;

/**
 * Created on 4.9.2014.
 *
 * @author Petri Airio <petri.j.airio@>
 */
public interface RestCommunicator {

  void sendRestMessage(RestMessage message, RestResponseHandler responseHandler);

}
