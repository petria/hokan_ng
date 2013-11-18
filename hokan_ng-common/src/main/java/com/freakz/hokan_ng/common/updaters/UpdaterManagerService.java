package com.freakz.hokan_ng.common.updaters;

import java.util.Collection;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 2:24 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UpdaterManagerService {

  Collection<Updater> getUpdaterList();

  Updater getUpdater(String updaterName);

  void stop();

}
