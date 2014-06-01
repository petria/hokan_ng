package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.SearchReplace;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by pairio on 31.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class SrListCmd extends Cmd {

  public SrListCmd() {
    super();
    setHelp("Lists Search / Replaces.");
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    List<SearchReplace> list = searchReplaceService.getSearchReplaces();
    StringBuilder sb = new StringBuilder("Search/Replace list: ");
    if (list.size() > 0) {
      boolean first = true;
      for (SearchReplace sr : list) {
        if (first) {
          first = false;
        } else {
          sb.append(", ");
        }
        sb.append(String.format("%d) s/%s/%s/", sr.getId(), sr.getSearch(), sr.getReplace()));
      }
    } else {
      sb.append("<empty>");
    }
    response.addResponse(sb);
    response.setNoSearchReplace(true);
  }
}
