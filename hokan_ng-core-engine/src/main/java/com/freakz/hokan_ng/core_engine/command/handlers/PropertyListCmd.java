package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 2:16 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class PropertyListCmd extends Cmd {

  public PropertyListCmd() {
    super();
    setHelp("Shows available properties.");
    addToHelpGroup(HelpGroup.PROPERTIES, this);
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    StringBuilder sb = new StringBuilder();
    for (Object property : PropertyName.values()) {
      sb.append(property);
      sb.append(" | ");
    }
    response.addResponse("Available properties: ");
    response.addResponse(sb.toString());
  }

}
