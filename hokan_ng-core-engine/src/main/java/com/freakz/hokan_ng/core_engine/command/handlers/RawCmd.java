package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_RAWSTRING;

/**
 * User: petria
 * Date: 12/10/13
 * Time: 12:28 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
@Component
public class RawCmd extends Cmd {

  public RawCmd() {
    super();
    setHelp("Sends raw command to the IRCd where this command was issued.");

    UnflaggedOption flg = new UnflaggedOption(ARG_RAWSTRING)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    setMasterUserOnly(true);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String raw = results.getString(ARG_RAWSTRING);
    response.addEngineMethodCall("sendRawLine", raw);
    response.addResponse("Sending: %s", raw);
  }

}
