package com.freakz.hokan_ng.common.updaters;

import java.util.List;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 2:24 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UpdaterManagerService {

  List<DataUpdater> getUpdaterList();

  DataUpdater getUpdater(String updaterName);

  void stop();

  void start();

  void startUpdater(DataUpdater updater);
}
