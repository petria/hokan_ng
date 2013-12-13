package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.TvNotifyDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.TvNotify;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import com.freakz.hokan_ng.common.updaters.telkku.TelkkuProgram;
import com.freakz.hokan_ng.common.updaters.telkku.TelkkuService;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 6:38 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class TvNotifyServiceImpl implements TvNotifyService {

  @Autowired
  private TvNotifyDAO tvNotifyDAO;

  @Autowired
  private TelkkuService tv;


  @Override
  public TvNotify addTvNotify(Channel channel, String pattern, String owner) {
    return tvNotifyDAO.addTvNotify(channel, pattern, owner);
  }

  @Override
  public List<TvNotify> getTvNotifies(Channel channel) {
    try {
      return tvNotifyDAO.getTvNotifies(channel);
    } catch (HokanDAOException e) {
      log.error("TvNotify error!", e);
    }
    return null;
  }

  @Override
  public TvNotify getTvNotify(Channel channel, String pattern) {
    try {
      return tvNotifyDAO.getTvNotify(channel, pattern);
    } catch (HokanDAOException e) {
      if (!e.getMessage().contains("javax.persistence.NoResultException")) {
        log.error("TvNotify error!", e);
      }
    }
    return null;
  }

  @Override
  public TvNotify getTvNotifyById(long id) {
    return tvNotifyDAO.getTvNotifyById(id);
  }

  @Override
  public int delTvNotifies(Channel channel) {
    return tvNotifyDAO.delTvNotifies(channel);
  }

  @Override
  public void delTvNotify(TvNotify notify) {
    tvNotifyDAO.delTvNotify(notify);
  }

  @Override
  public List<TelkkuProgram> getChannelDailyNotifiedPrograms(Channel channel, Date day) {
    List<TelkkuProgram> matches = new ArrayList<>();
    List<TelkkuProgram> daily = tv.findDailyPrograms(day);
    List<TvNotify> notifies = getTvNotifies(channel);
    for (TelkkuProgram prg : daily) {
      for (TvNotify notify : notifies) {
        if (StringStuff.match(prg.getProgram(), ".*" + notify.getNotifyPattern() + ".*", true)) {
          matches.add(prg);
        }
      }
    }
    return matches;
  }
}
