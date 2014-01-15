package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.Properties;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 2:20 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class ChanEnvCmd extends Cmd {

  @Autowired
  private Properties properties;

  public ChanEnvCmd() {
    super();
    setHelp("Shows properties set for the channel");
    addToHelpGroup(HelpGroup.PROPERTIES, this);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest iRequest = (InternalRequest) request;
    List propertyList = properties.getChannelProperties(iRequest.getChannel());
    for (Object property : propertyList) {
      response.addResponse("%s\n", property.toString());
    }
  }
}
