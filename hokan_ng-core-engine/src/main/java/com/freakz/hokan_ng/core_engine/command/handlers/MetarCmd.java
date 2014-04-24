package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.core_engine.service.MetarData;
import com.freakz.hokan_ng.core_engine.service.MetarDataService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_STATION;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 1:18 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class MetarCmd extends Cmd {

  @Autowired
  private MetarDataService metarDataService;

  public MetarCmd() {
    super();
    setHelp("Queries Metar weather datas. See: http://en.wikipedia.org/wiki/METAR");

    UnflaggedOption opt = new UnflaggedOption(ARG_STATION)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);
  }

  @Override
  public String getMatchPattern() {
    return "!metar.*";
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String station = results.getString(ARG_STATION);
    List<MetarData> metarDatas = metarDataService.getMetarData(station);
    if (metarDatas.size() > 0) {
      StringBuilder sb = new StringBuilder();
      for (MetarData metarData : metarDatas) {
        if (sb.length() > 0) {
          sb.append(" | ");
        }
        sb.append(metarData.getMetarData());
      }
      response.addResponse(sb.toString());
    } else {
      response.addResponse("No Metar data found with: " + station);
    }
  }

}
