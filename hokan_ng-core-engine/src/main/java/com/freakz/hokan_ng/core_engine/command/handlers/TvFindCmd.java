package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.updaters.telkku.TelkkuProgram;
import com.freakz.hokan_ng.common.updaters.telkku.TelkkuService;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PROGRAM;

/**
 * User: petria
 * Date: 11/27/13
 * Time: 3:45 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class TvFindCmd extends Cmd {

  @Autowired
  private TelkkuService tv;

  public TvFindCmd() {
    super();
    setHelp("Finds Telkku programs.");
    addToHelpGroup(HelpGroup.TV, this);


    UnflaggedOption opt = new UnflaggedOption(ARG_PROGRAM)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    if (!tv.isReady()) {
      response.addResponse("Telkku data not loaded yet! Try again later...");
      return;
    }
    String program = results.getString(ARG_PROGRAM);
    List<TelkkuProgram> matching = tv.findPrograms(program);
    if (matching.size() > 0) {
      String reply = "";
      String lastChannel = "";
      for (TelkkuProgram prg : matching) {
        SimpleDateFormat dateFormat;
        if (StringStuff.isDateToday(prg.getStartTimeD())) {
          dateFormat = StringStuff.STRING_STUFF_DF_HHMM;
        } else {
          dateFormat = StringStuff.STRING_STUFF_DF_DDMMHHMM;
        }

        String channel = prg.getChannel();
        if (!channel.equalsIgnoreCase(lastChannel)) {
          reply += "[" + channel + "] ";
        }
        lastChannel = channel;
        reply += StringStuff.formatTime(prg.getStartTimeD(), dateFormat) +
            " " + prg.getProgram() + "(" + prg.getId() + ") ";

      }
      response.addResponse(reply);
    } else {
      response.addResponse("No matching Telkku programs found with: " + program);
    }
  }
}
