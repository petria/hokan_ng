package com.freakz.hokan_ng.common.updaters;

import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
import com.freakz.hokan_ng.common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 2:31 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public abstract class Updater implements DataUpdater, CommandRunnable {

  protected long updateCount;
  private Calendar nextUpdate = TimeUtil.getCalendar();
  private Calendar lastUpdate;
  protected UpdaterStatus status;

  protected Updater() {
  }

  @Override
  public String getUpdaterName() {
    return this.getClass().getSimpleName();
  }

  @Override
  public Calendar getNextUpdateTime() {
    return nextUpdate;
  }

  @Override
  public Calendar getLastUpdateTime() {
    return lastUpdate;
  }

  @Override
  public Calendar calculateNextUpdate() {
    Calendar cal = TimeUtil.getCalendar();
    cal.add(Calendar.MINUTE, 5);
    return cal;
  }

  @Override
  public void updateData(CommandPool commandPool) {
    commandPool.startRunnable(this);
  }

  @Override
  public void handleRun(long myPid, Object args) {
    try {
      status = UpdaterStatus.UPDATING;
      doUpdateData();
      updateCount++;
      status = UpdaterStatus.IDLE;
      lastUpdate = TimeUtil.getCalendar();

    } catch (Exception e) {
      log.error("Updater failed", e);
    }
    nextUpdate = calculateNextUpdate();
  }


  public abstract void doUpdateData() throws Exception;

  @Override
  public void getData(UpdaterData data, String... args) {
    data.setData(doGetData(args));
  }

  @Override
  public UpdaterData getData(String... args) {
    UpdaterData data = new UpdaterData();
    data.setData(doGetData(args));
    return data;
  }

  public abstract Object doGetData(String... args);

  @Override
  public UpdaterStatus getStatus() {
    return this.status;
  }

  @Override
  public long getUpdateCount() {
    return this.updateCount;
  }

}
