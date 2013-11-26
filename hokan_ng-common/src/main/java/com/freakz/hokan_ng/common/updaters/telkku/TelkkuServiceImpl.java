package com.freakz.hokan_ng.common.updaters.telkku;

import com.freakz.hokan_ng.common.updaters.UpdaterManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 2:30 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class TelkkuServiceImpl implements TelkkuService {

  @Autowired
  private UpdaterManagerService updaterManagerService;

//  private DataUpdater telkkuUpdater;

  public TelkkuServiceImpl() {

  }

  @Override
  public TelkkuProgram getCurrentProgram(Date time, String channelMatcher) {
    TelkkuData telkkuData = (TelkkuData) updaterManagerService.getUpdater("telkkuUpdater").getData().getData();
    if (telkkuData.getPrograms() == null) {
      return null;
    }
    for (TelkkuProgram tp : telkkuData.getPrograms()) {
      if (tp.getChannel().equalsIgnoreCase(channelMatcher)) {
        if (time.getTime() > tp.getStartTimeD().getTime())
          return tp;
      }
    }
    return null;
  }

  @Override
  public TelkkuProgram getNextProgram(TelkkuProgram current, String channel) {
    TelkkuData telkkuData = (TelkkuData) updaterManagerService.getUpdater("telkkuUpdater").getData().getData();
    if (telkkuData.getPrograms() == null) {
      return null;
    }
    TelkkuProgram tp;
    Enumeration eNum = Collections.enumeration(telkkuData.getPrograms());
    boolean found = false;
    while (eNum.hasMoreElements()) {
      tp = (TelkkuProgram) eNum.nextElement();
      if (tp == current) {
        found = true;
        break;
      }
    }
    if (found) {
      while (eNum.hasMoreElements()) {
        tp = (TelkkuProgram) eNum.nextElement();
        if (tp.getChannel().equalsIgnoreCase(current.getChannel())) {
          return tp;
        }
      }
    }
    return null;
  }

  @Override
  public boolean isReady() {
    return updaterManagerService.getUpdater("telkkuUpdater").getUpdateCount() > 0;
  }

  @Override
  public String[] getChannels() {
    return new String[]{"YLE TV1", "YLE TV2", "MTV3", "Nelonen", "Kutonen", "JIM", "Sub", "YLE Teema", "TV5", "Fox", "Yle Fem"};
  }

}
