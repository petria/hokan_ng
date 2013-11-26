package com.freakz.hokan_ng.common.updaters;

import com.freakz.hokan_ng.common.engine.CommandPool;

import java.util.Calendar;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 2:25 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface DataUpdater {

  Calendar getNextUpdateTime();

  Calendar getLastUpdateTime();

  Calendar calculateNextUpdate();

  void updateData(CommandPool commandPool);

  void getData(UpdaterData updaterData, String... args);

  UpdaterStatus getStatus();

  long getUpdateCount();

  String getUpdaterName();


}
