package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.TvNotifyDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.TvNotify;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
