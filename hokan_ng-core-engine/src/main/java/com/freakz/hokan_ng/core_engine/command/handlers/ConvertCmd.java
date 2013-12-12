package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.util.HttpPageFetcher;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 12/12/13
 * Time: 10:17 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class ConvertCmd extends Cmd {

  private static final String ARG_AMOUNT = "Amount";
  private static final String ARG_FROM = "From";
  private static final String ARG_TO = "To";

  public ConvertCmd() {
    super();
    setHelp("ConvertCmd help");

    UnflaggedOption flg = new UnflaggedOption(ARG_AMOUNT)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    flg = new UnflaggedOption(ARG_FROM)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    flg = new UnflaggedOption(ARG_TO)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String amount = results.getString(ARG_AMOUNT);
    String from = results.getString(ARG_FROM);
    String to = results.getString(ARG_TO);

    String changed = doConvert(amount, from, to);

    if (!changed.contains("=")) {
      changed = "Epic failure: check parameters!";
    }
    response.addResponse(changed);
  }

  public String doConvert(String amount, String from, String to) {

    String url = "http://www.google.com/finance/converter?a=" + amount + "&from=" + from + "&to=" + to;
    HttpPageFetcher page;
    try {
      page = new HttpPageFetcher(url);
    } catch (Exception e) {
      return e.getMessage();
    }

    String[] text = page.getTextBuffer().toString().split("\n");
    String lastLine = text[text.length - 1];
    return lastLine.trim();
  }


}
