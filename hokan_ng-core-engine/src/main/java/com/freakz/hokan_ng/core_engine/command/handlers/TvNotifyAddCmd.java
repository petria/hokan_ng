package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.TvNotify;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.TvNotifyService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PROGRAM;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 6:39 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class TvNotifyAddCmd extends Cmd {

  @Autowired
  private TvNotifyService tvNotifyService;

  public TvNotifyAddCmd() {
    super();
    setHelp("Adds notify for Telkku programs.");
    addToHelpGroup(HelpGroup.TV, this);

    UnflaggedOption opt = new UnflaggedOption(ARG_PROGRAM)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest iRequest = (InternalRequest) request;
    TvNotify notify = tvNotifyService.getTvNotify(iRequest.getChannel(), results.getString(ARG_PROGRAM));
    if (notify != null) {
      response.addResponse("TvNotify: %d: %s already in notify list!", notify.getId(), notify.getNotifyPattern());
      return;
    }
    notify = tvNotifyService.addTvNotify(iRequest.getChannel(), results.getString(ARG_PROGRAM), iRequest.getIrcEvent().getSender());
    response.addResponse("Added TvNotify: %d: %s", notify.getId(), notify.getNotifyPattern());
  }

}
