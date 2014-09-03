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

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_ID_OR_SEARCH;

/**
 * Created by pairio on 31.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class SrDelCmd extends Cmd {

  public SrDelCmd() {
    super();
    setHelp("Deletes Search/Replace by id or search keyword.");

    UnflaggedOption opt = new UnflaggedOption(ARG_ID_OR_SEARCH)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String idORSearch = results.getString(ARG_ID_OR_SEARCH);
    long id = 0;
    try {
      id = Long.parseLong(idORSearch);
    } catch (NumberFormatException nfe) {
      id = -1;
    }
    if (id == -1) {

      List<SearchReplace> srList = searchReplaceService.findSearchReplaces(idORSearch);

      if (srList.size() > 0) {
        for (SearchReplace sr : srList) {
          searchReplaceService.deleteSearchReplace(sr);
          response.addResponse("Removed Search/Replace: [%2d] s/%s/%s/", sr.getId(), sr.getSearch(), sr.getReplace());
        }
      } else {
        response.addResponse("No matching Search/Replaces found with: %s", idORSearch);
      }

    } else {
      SearchReplace sr = searchReplaceService.getSearchReplace(id);
      searchReplaceService.deleteSearchReplace(sr);
      response.addResponse("Removed Search/Replace: [%2d] s/%s/%s/", sr.getId(), sr.getSearch(), sr.getReplace());
    }
  }
}
