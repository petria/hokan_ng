package com.freakz.hokan_ng.common.updaters;

import com.freakz.hokan_ng.common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 2:31 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public abstract class AbstractDataUpdater implements DataUpdater {

  private long updateCount;
  protected UpdaterStatus status;

  protected AbstractDataUpdater() {
  }

  @Override
  public String getUpdaterName() {
    return this.getClass().getSimpleName();
  }

  @Override
  public Date getNextUpdateTime() {
    Calendar cal = TimeUtil.getCalendar();
    cal.add(Calendar.MINUTE, 5);
    return cal.getTime();
  }

  @Override
  public void updateData() {
    try {
      status = UpdaterStatus.UPDATING;
      doUpdateData();
      updateCount++;
      status = UpdaterStatus.IDLE;
    } catch (Exception e) {
      log.error("Updater failed", e);
    }
  }

  public abstract void doUpdateData();

  @Override
  public void getData(UpdaterData data) {
    data.setData(doGetData());
  }

  public abstract Object doGetData();

  @Override
  public UpdaterStatus getStatus() {
    return this.status;
  }

  @Override
  public long getUpdateCount() {
    return this.updateCount;
  }

}
