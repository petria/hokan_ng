package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 1/3/14
 * Time: 1:14 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class SpreeCmd extends Cmd {

  public SpreeCmd() {
    super();
    setHelp("Shows writer spree owner / count in Channel.");
    addToHelpGroup(HelpGroup.CHANNELS, this);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    Channel channel = ir.getChannel();
    response.addResponse("%s writing spree owner: %s with %d lines!", channel.getChannelName(), channel.getWriterSpreeOwner(), channel.getWriterSpreeRecord());
  }

}
