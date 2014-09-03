package com.freakz.hokan_ng.common.rest.messages;

import java.io.Serializable;

/**
 * Created on 3.9.2014.
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class RestBaseMessage implements Serializable {

  private RestMessageTarget restMessageTarget;

  private RestRequestResponse restRequestResponse;

}
