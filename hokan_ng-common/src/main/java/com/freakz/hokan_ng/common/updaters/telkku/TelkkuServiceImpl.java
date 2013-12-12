package com.freakz.hokan_ng.common.updaters.telkku;

import com.freakz.hokan_ng.common.engine.AsyncCoreMessageSender;
import com.freakz.hokan_ng.common.engine.CoreEventHandler;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.entity.TvNotify;
import com.freakz.hokan_ng.common.rest.CoreRequest;
import com.freakz.hokan_ng.common.rest.CoreResponse;
import com.freakz.hokan_ng.common.service.Properties;
import com.freakz.hokan_ng.common.service.TvNotifyService;
import com.freakz.hokan_ng.common.updaters.UpdaterManagerService;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 2:30 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class TelkkuServiceImpl implements TelkkuService, CoreEventHandler {

  @Autowired
  private UpdaterManagerService updaterManagerService;

  @Autowired
  private Properties properties;

  @Autowired
  private TvNotifyService notifyService;

  @Autowired
  private ApplicationContext context;

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
        Date start = tp.getStartTimeD();
        Date end = tp.getEndTimeD();
        if (start.getTime() < time.getTime() && end.getTime() > time.getTime())
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
  public List<TelkkuProgram> findPrograms(String program) {
    List<TelkkuProgram> matches = new ArrayList<>();
    TelkkuData telkkuData = (TelkkuData) updaterManagerService.getUpdater("telkkuUpdater").getData().getData();
    if (telkkuData.getPrograms() == null) {
      return matches;
    }
    for (TelkkuProgram tp : telkkuData.getPrograms()) {
      if (StringStuff.match(tp.getProgram(), ".*" + program + ".*", true)) {
        matches.add(tp);
      }
    }
    return matches;
  }

  @Override
  public TelkkuProgram findProgramById(int id) {
    TelkkuData telkkuData = (TelkkuData) updaterManagerService.getUpdater("telkkuUpdater").getData().getData();
    if (telkkuData.getPrograms() == null) {
      return null;
    }
    for (TelkkuProgram tp : telkkuData.getPrograms()) {
      if (tp.getId() == id) {
        return tp;
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

  private static class Notify {
    public Channel channel;
    public TvNotify notify;
    public TelkkuProgram program;
  }

  @Scheduled(initialDelay = 5000, fixedDelay = 30000)
  public void notifyWatcher() {
    List<Channel> channels = properties.getChannelsWithProperty(PropertyName.PROP_CHANNEL_DO_TVNOTIFY);
    Calendar future = new GregorianCalendar();
    future.add(Calendar.MINUTE, 5);
    List<Notify> toNotify = new ArrayList<>();
    for (String tvChannel : getChannels()) {
      for (Channel channel : channels) {
        List<TvNotify> notifyList = notifyService.getTvNotifies(channel);
        TelkkuProgram current = getCurrentProgram(future.getTime(), tvChannel);
        if (current != null) {
          for (TvNotify notify : notifyList) {
            if (StringStuff.match(current.getProgram(), ".*" + notify.getNotifyPattern() + ".*", true)) {
              Notify n = new Notify();
              n.channel = channel;
              n.notify = notify;
              n.program = current;
              toNotify.add(n);
            }
          }
        }
      }
    }
    if (toNotify.size() > 0) {
      sendNotifies(toNotify);
    }

  }

  private void sendNotifies(List<Notify> toNotify) {
    AsyncCoreMessageSender sender = context.getBean(AsyncCoreMessageSender.class);
    for (Notify notify : toNotify) {

      CoreRequest request = new CoreRequest();
      request.setTargetChannelId(notify.channel.getChannelId());
      request.setMessage("fufufufuf tvnotify!");
//      sender.sendRequest(request, this);

    }
  }

  @Override
  public void handleCoreResponse(CoreResponse response) {
    log.info("here!");
  }

  @Override
  public void handleCoreError(CoreResponse response) {
    log.info("here!");
  }


}
