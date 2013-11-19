package com.freakz.hokan_ng.common.updaters;

import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
import com.freakz.hokan_ng.common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 8:19 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class UpdateManagerServiceImpl implements UpdaterManagerService, CommandRunnable {

  @Autowired
  private ApplicationContext context;

  @Autowired
  private CommandPool commandPool;

  private Map<String, Updater> handlers;
  private boolean doRun;
  private boolean firstRun = true;

  public UpdateManagerServiceImpl() {
  }

  @PostConstruct
  public void refreshHandlers() {
    handlers = context.getBeansOfType(Updater.class);
  }


  public void stop() {
    this.doRun = false;
  }

  @Override
  public void start() {
    commandPool.startRunnable(this);
  }

  @Override
  public Collection<Updater> getUpdaterList() {
    return handlers.values();
  }

  public Updater getUpdater(String updaterName) {
    return handlers.get(updaterName);
  }

  @Override
  public void handleRun(long myPid, Object args) {
    doRun = true;
    log.info("<< Starting update service: {} >>", myPid);
    while (doRun) {
//      log.info("ping");
      for (Updater updater : getUpdaterList()) {
        Calendar now = TimeUtil.getCalendar();
        Calendar next = updater.getNextUpdateTime();
        if (firstRun || now.after(next)) {
          if (updater.getStatus() != UpdaterStatus.UPDATING) {
            updater.updateData(this.commandPool);
          }
        }
      }
      firstRun = false;
      try {
        Thread.sleep(5000 * 2);
      } catch (InterruptedException e) {
        // ignore
      }
    }
    log.info("<< Stoping update service: {} >>", myPid);
  }
}
