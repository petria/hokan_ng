package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User: petria
 * Date: 12/3/13
 * Time: 11:39 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
@Service
public class SystemTimerImpl implements SystemTimer, CommandRunnable {

  private static final int SYSTEM_TIMER_SLEEP = 5;

  @Autowired
  private CommandPool commandPool;

  private long tickCount;
  private List<SystemTimerUser> users = new ArrayList<>();
  private boolean running = true;

  public SystemTimerImpl() {
  }

  @Override
  public void addSystemTimerUser(SystemTimerUser user) {
    users.add(user);
  }

  @Override
  public void start() {
    commandPool.startRunnable(this);
  }

  @Override
  public void stop() {
    this.running = false;
  }

  @Override
  public void handleRun(long myPid, Object args) {
    log.info("Starting system timer, pid: {}", myPid);
    while (this.running) {
      Calendar calendar = new GregorianCalendar();
      int hh = calendar.get(Calendar.HOUR_OF_DAY);
      int mm = calendar.get(Calendar.MINUTE);
      int ss = calendar.get(Calendar.SECOND);

      for (SystemTimerUser user : this.users) {
        this.tickCount++;
        try {
          user.timerTick(calendar, hh, mm, ss);
        } catch (Exception e) {
          log.error("SystemTimerUser failed {}", user, e);
        }
      }
      try {
        Thread.sleep(1000 * SYSTEM_TIMER_SLEEP);
      } catch (InterruptedException e) {
        //
      }
    }
    log.info("System timer stopped, pid: {}", myPid);
  }

  @Override
  public long getTickCount() {
    return this.tickCount;
  }

}
