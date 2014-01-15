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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PROGRAM;

/**
 * User: petria
 * Date: 12/12/13
 * Time: 6:06 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class TvNotifyDelCmd extends Cmd {

  @Autowired
  private TvNotifyService tvNotifyService;

  public TvNotifyDelCmd() {
    super();
    setHelp("Removes TvNotify either by Id or by keyword");
    addToHelpGroup(HelpGroup.TV, this);

    UnflaggedOption opt = new UnflaggedOption(ARG_PROGRAM)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;

    String program = results.getString(ARG_PROGRAM);
    if (program.equals("all")) {
      int removed = tvNotifyService.delTvNotifies(ir.getChannel());
      response.addResponse("Removed %d TvNotifies.", removed);
      return;
    } else {
      TvNotify notify;
      try {
        long id = Long.parseLong(program);
        notify = tvNotifyService.getTvNotifyById(id);
      } catch (NumberFormatException e) {
        notify = tvNotifyService.getTvNotify(ir.getChannel(), program);
      }
      if (notify != null) {
        tvNotifyService.delTvNotify(notify);
        response.addResponse("Removed TvNotify: %d: %s", notify.getId(), notify.getNotifyPattern());

      } else {
        response.addResponse("No TvNotify found with: %s", program);
      }
    }
  }
}
