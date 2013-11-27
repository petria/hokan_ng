package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: petria
 * Date: 11/27/13
 * Time: 11:14 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class DateCmd extends Cmd {

  public DateCmd() {
    super();
    setHelp("Shows system date and time.");
  }

  @Override
  public String getMatchPattern() {
    return "!date";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    response.addResponse(new Date().toString());
  }

}
