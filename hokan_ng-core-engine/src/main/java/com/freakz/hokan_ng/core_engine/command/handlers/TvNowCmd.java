package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.updaters.telkku.TelkkuProgram;
import com.freakz.hokan_ng.common.updaters.telkku.TelkkuService;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 12:53 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class TvNowCmd extends Cmd {

  @Autowired
  private TelkkuService tv;

  public TvNowCmd() {
    super();
    setHelp("Shows what's going on in TV.");
    addSeeAlso("TvFind");
    addSeeAlso("TvInfo");

  }

  @Override
  public String getMatchPattern() {
    return "!tvnow.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    if (!tv.isReady()) {
      response.addResponse("Telkku data not loaded yet! Try again later...");
      return;
    }
    String currentLine = "";
    String nextLine = "";


    for (String channel : tv.getChannels()) {
      TelkkuProgram current = tv.getCurrentProgram(new Date(), channel);
      TelkkuProgram next = tv.getNextProgram(current, channel);

      if (current != null) {
        currentLine +=
            "[" + channel + "] " +
                StringStuff.formatTime(current.getStartTimeD(), StringStuff.STRING_STUFF_DF_HHMM) +
                " " + current.getProgram() + "(" + current.getId() + ") ";
      }

      if (next != null) {
        nextLine +=
            "[" + channel + "] " +
                StringStuff.formatTime(next.getStartTimeD(), StringStuff.STRING_STUFF_DF_HHMM) +
                " " + next.getProgram() + "(" + next.getId() + ") ";
      }

    }

    response.addResponse(currentLine + "\n");
    response.addResponse(nextLine);
  }
}
