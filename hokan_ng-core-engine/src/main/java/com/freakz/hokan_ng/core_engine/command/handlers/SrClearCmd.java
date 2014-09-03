package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by pairio on 31.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class SrClearCmd extends Cmd {

  public SrClearCmd() {
    super();
    setHelp("Clears Search/Replaces");
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    searchReplaceService.deleteSearchReplaces();
    response.addResponse("Search/Replaces cleared!");
  }

}
