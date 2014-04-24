package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.updaters.UpdaterData;
import com.freakz.hokan_ng.common.updaters.UpdaterManagerService;
import com.freakz.hokan_ng.common.updaters.horo.HoroHolder;
import com.freakz.hokan_ng.common.updaters.horo.HoroUpdater;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_HORO;

/**
 * User: petria
 * Date: 11/21/13
 * Time: 1:43 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class HoroCmd extends Cmd {

  @Autowired
  private UpdaterManagerService updaterManagerService;

  public HoroCmd() {
    super();

    UnflaggedOption opt = new UnflaggedOption(ARG_HORO)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public String getMatchPattern() {
    return "!horo.*";
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String horo = results.getString(ARG_HORO);
    HoroUpdater horoUpdater = (HoroUpdater) updaterManagerService.getUpdater("horoUpdater");

    UpdaterData updaterData = new UpdaterData();
    horoUpdater.getData(updaterData, horo);
    HoroHolder hh = (HoroHolder) updaterData.getData();
    if (hh != null) {
      response.setResponseMessage(hh.toString());
    } else {
      response.setResponseMessage("Saat dildoo perään ja et pääse pylsimään!");
    }
  }

}
