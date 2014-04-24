package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.updaters.DataUpdater;
import com.freakz.hokan_ng.common.updaters.UpdaterManagerService;
import com.freakz.hokan_ng.common.updaters.UpdaterStatus;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_UPDATER;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 11:13 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class UpdaterStartCmd extends Cmd {

  @Autowired
  private UpdaterManagerService updaterManagerService;

  public UpdaterStartCmd() {
    super();
    setHelp("Starts specific updater.");

    UnflaggedOption opt = new UnflaggedOption(ARG_UPDATER)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

    addToHelpGroup(HelpGroup.UPDATERS, this);
    setMasterUserOnly(true);
  }

  @Override
  public String getMatchPattern() {
    return "!updaterstart.*";
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String updaterName = results.getString(ARG_UPDATER);
    DataUpdater updater = updaterManagerService.getUpdater(updaterName);
    if (updater != null) {
      if (updater.getStatus() == UpdaterStatus.UPDATING) {
        response.addResponse("Updater is already running, see !updaterlist");
      } else {
        updaterManagerService.startUpdater(updater);
        response.addResponse("Started updater: " + updater.getUpdaterName());
      }
    } else {
      response.addResponse("Updater not found: " + updaterName + "\n");
      response.addResponse("Use !updaterlist to see updaters");
    }
  }
}
