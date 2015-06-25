package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.updaters.DataUpdater;
import com.freakz.hokan_ng.common.updaters.KelikameratWeatherData;
import com.freakz.hokan_ng.common.updaters.UpdaterData;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_COUNT;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PLACE;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 9:07 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class WeatherCmd extends Cmd {

  public WeatherCmd() {
    super();
    setHelp("Shows weather information from http://www.kelikamerat.info/.");
    addToHelpGroup(HelpGroup.UPDATERS, this);

    FlaggedOption flg = new FlaggedOption(ARG_COUNT)
        .setStringParser(JSAP.INTEGER_PARSER)
        .setDefault("5")
        .setRequired(true)
        .setShortFlag('c');
    registerParameter(flg);

    UnflaggedOption opt = new UnflaggedOption(ARG_PLACE)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public String getMatchPattern() {
    return "!saa.*|!weather.*";
  }

  private String formatWeather(KelikameratWeatherData d) {
    return String.format("%s: ilma %2.1f°C tie %2.1f°C maa %2.1f°C",
            d.getPlaceFromUrl(), d.getAir(), d.getRoad(), d.getGround());
  }
  @Override
  @SuppressWarnings("unchecked")
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    DataUpdater weatherUpdater = updaterManagerService.getUpdater("kelikameratUpdater");
    UpdaterData updaterData = new UpdaterData();
    weatherUpdater.getData(updaterData);

    List<KelikameratWeatherData> datas = (List<KelikameratWeatherData>) updaterData.getData();
    if (datas.size() == 0) {
      response.setResponseMessage("Weather data not ready yet!");
      return;
    }

    String place = results.getString(ARG_PLACE).toLowerCase();
    StringBuilder sb = new StringBuilder();

    if (place.equals("minmax")) {

      KelikameratWeatherData max = datas.get(0);
      KelikameratWeatherData min = datas.get(datas.size() - 1);

      sb.append("Min: ");
      sb.append(formatWeather(min));
      sb.append(" Max: ");
      sb.append(formatWeather(max));

    } else {

      int xx = 0;
      String regexp = ".*" + place + ".*";
      for (KelikameratWeatherData wd : datas) {
        if (StringStuff.match(wd.getPlaceFromUrl(), regexp) || StringStuff.match(wd.getUrl().getStationUrl(), regexp)) {
          if (wd.getAir() == null) {
            continue;
          }
          if (xx != 0) {
            sb.append(", ");
          }
          sb.append(formatWeather(wd));
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
