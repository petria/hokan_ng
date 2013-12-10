package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.Properties;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/10/13
 * Time: 2:32 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
@Component
public class EnvCmd extends Cmd {

  @Autowired
  private Properties properties;

  public EnvCmd() {
    super();
    setHelp("Show bot system properties.");
    addSeeAlso("!set");
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    List propertyList = properties.getAllProperties();
    for (Object property : propertyList) {
      response.addResponse("%s\n", property.toString());
    }
  }
}
