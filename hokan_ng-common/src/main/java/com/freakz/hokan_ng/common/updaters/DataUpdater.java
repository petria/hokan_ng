package com.freakz.hokan_ng.common.updaters;

import java.util.Date;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 2:25 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface DataUpdater {

  Date getNextUpdateTime();

  void updateData();

  void getData(UpdaterData updaterData);

  UpdaterStatus getStatus();

  long getUpdateCount();

  String getUpdaterName();

}
