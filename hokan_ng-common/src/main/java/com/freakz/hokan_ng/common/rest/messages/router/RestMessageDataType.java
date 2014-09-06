package com.freakz.hokan_ng.common.rest.messages.router;

import java.io.Serializable;

/**
 * Created on 4.9.2014.
 *
 * @author Petri Airio <petri.j.airio@>
 */
public enum RestMessageDataType implements Serializable {

  IO_REQUEST,
  IO_RESPONSE,

  ENGINE_REQUEST,
  ENGINE_RESPONSE,

  IRC_EVENT

}
