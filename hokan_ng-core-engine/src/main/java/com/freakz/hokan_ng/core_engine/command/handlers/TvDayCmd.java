package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.TvNotifyService;
import com.freakz.hokan_ng.common.updaters.telkku.TelkkuProgram;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * User: petria
 * Date: 12/13/13
 * Time: 8:43 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class TvDayCmd extends Cmd {

  @Autowired
  private TvNotifyService tvNotifyService;

  public TvDayCmd() {
    super();
    setHelp("Is there something interesting (=notified) programs coming from TV?");
    addToHelpGroup(HelpGroup.TV, this);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    List<TelkkuProgram> daily = tvNotifyService.getChannelDailyNotifiedPrograms(ir.getChannel(), new Date());
    if (daily.size() == 0) {
      response.addResponse("Nothing interesting in TV today!");
      return;
    }
    String lastChannelStr = "";
    int i = 0;
    String reply = "Programs on notify today: ";
    for (TelkkuProgram prg : daily) {
      String channelStr = "[" + prg.getChannel() + "] ";
      if (channelStr.equalsIgnoreCase(lastChannelStr)) {
        lastChannelStr = channelStr;
        channelStr = "";
      } else {
        lastChannelStr = channelStr;
      }
      reply += String.format("%d) %s%s: %s(%d) ", i + 1, channelStr, StringStuff.formatTime(prg.getStartTimeD(), StringStuff.STRING_STUFF_DF_HHMM), prg.getProgram(), prg.getId());
      i++;
    }
    response.addResponse(reply);
  }

}
