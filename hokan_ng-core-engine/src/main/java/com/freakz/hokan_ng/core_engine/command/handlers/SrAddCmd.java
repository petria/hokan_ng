package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.SearchReplace;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_REPLACE;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_SEARCH;

/**
 * Created by pairio on 28.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class SrAddCmd extends Cmd {

  public SrAddCmd() {
    super();
    setHelp("Adds Search/Replace.");

    UnflaggedOption opt = new UnflaggedOption(ARG_SEARCH)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

    opt = new UnflaggedOption(ARG_REPLACE)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String search = results.getString(ARG_SEARCH);
    String replace = results.getString(ARG_REPLACE);

    SearchReplace sr = searchReplaceService.addSearchReplace(request.getIrcEvent().getSender(), search, replace);
    if (sr != null) {
      response.addResponse("Added Search/Replace: [%2d] s/%s/%s/", sr.getId(), sr.getSearch(), sr.getReplace());
      response.setNoSearchReplace(true);
    } else {
      response.addResponse("Added Search/Replace failed!");
    }

  }

}
