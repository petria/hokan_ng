package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.TvNotify;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.TvNotifyService;
import com.freakz.hokan_ng.core_engine.dto.InternalRequest;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 7:04 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class TvNotifyListCmd extends Cmd {

  @Autowired
  private TvNotifyService tvNotifyService;

  public TvNotifyListCmd() {
    super();
    setHelp("Show channels tvnotify list.");
    addToHelpGroup(HelpGroup.TV, this);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest iRequest = (InternalRequest) request;
    List<TvNotify> notifies = tvNotifyService.getTvNotifies(iRequest.getChannel());
    response.addResponse("Keywords currently on TV notify in channel %s:", iRequest.getChannel().getChannelName());
    for (TvNotify notify : notifies) {
      response.addResponse(" %d: %s", notify.getId(), notify.getNotifyPattern());
    }
  }

}
