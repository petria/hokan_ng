package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.updaters.DataUpdater;
import com.freakz.hokan_ng.common.updaters.UpdaterData;
import com.freakz.hokan_ng.common.updaters.UpdaterManagerService;
import com.freakz.hokan_ng.common.updaters.weather.WeatherData;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 9:07 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class WeatherCmd extends Cmd {

  @Autowired
  private UpdaterManagerService updaterManagerService;

  private final static String FORMAT = "%1 %2 %3°C (%7/%8)";
  private final static String FORMAT_MINMAX = "%1 %9 %3°C (%7/%8)";

  private final static String ARG_COUNT = "count";
  private final static String ARG_PLACE = "place";

  public WeatherCmd() {
    super();

    FlaggedOption flg = new FlaggedOption(ARG_COUNT)
        .setStringParser(JSAP.INTEGER_PARSER)
        .setDefault("5")
        .setRequired(true)
        .setShortFlag('c');
    registerParameter(flg);

    UnflaggedOption opt = new UnflaggedOption(ARG_PLACE)
        .setDefault("Jyväskylä")
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public String getMatchPattern() {
    return "!saa.*";
  }

  @Override
  @SuppressWarnings("unchecked")
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    DataUpdater weatherUpdater = updaterManagerService.getUpdater("weatherUpdater");
    UpdaterData updaterData = new UpdaterData();
    weatherUpdater.getData(updaterData);
    List<WeatherData> datas = (List<WeatherData>) updaterData.getData();
    if (datas.size() == 0) {
      response.setResponseMessage("Weather data not ready yet!");
      return;
    }
    String place = results.getString(ARG_PLACE).toLowerCase();
    StringBuilder sb = new StringBuilder();

    if (place.equals("minmax")) {

      WeatherData max = datas.get(0);
      WeatherData min = datas.get(datas.size() - 1);

      sb.append("Min: ");
      sb.append(StringStuff.fillTemplate(FORMAT_MINMAX, min.getData()));
      sb.append(" Max: ");
      sb.append(StringStuff.fillTemplate(FORMAT_MINMAX, max.getData()));

    } else {

      int xx = 0;
      for (WeatherData wd : datas) {
        if (StringStuff.match(wd.getCity(), ".*" + place + ".*")) {
          if (xx != 0) {
            sb.append(", ");
          }
          sb.append(StringStuff.fillTemplate(FORMAT, wd.getData()));
          xx++;
          if (xx > results.getInt(ARG_COUNT)) {
            break;
          }
        }
      }
      if (xx == 0) {
        String hhmmss = StringStuff.formatTime(new Date(), StringStuff.STRING_STUFF_DF_HHMMSS);
        sb.append(String.format("%s %s 26.7°C, hellettä pukkaa!", hhmmss, place));
      }
    }

    response.setResponseMessage(sb.toString());
  }
}
