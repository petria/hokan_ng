package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.updaters.DataUpdater;
import com.freakz.hokan_ng.common.updaters.UpdaterManagerService;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/22/13
 * Time: 10:54 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class UpdaterListCmd extends Cmd {

  @Autowired
  private UpdaterManagerService updaterManagerService;

  public UpdaterListCmd() {
    super();
    setHelp("Shows DataUpdaters and their status / update count / next update");
  }

  @Override
  public String getMatchPattern() {
    return "!updaterlist.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String header = String.format("%15s - %9s - %6s - %s\n", "Name", "Status", "Count", "Next update");
    response.addResponse(header);
    for (DataUpdater updater : updaterManagerService.getUpdaterList()) {
      String txt = String.format("%15s - %9s - %6d - %s\n", updater.getUpdaterName(), updater.getStatus(), updater.getUpdateCount(), updater.getNextUpdateTime().getTime());
      response.addResponse(txt);
    }
  }

}
