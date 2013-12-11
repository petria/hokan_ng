package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.TvNotify;
import com.freakz.hokan_ng.common.exception.HokanDAOException;

import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 6:27 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface TvNotifyDAO {

  TvNotify addTvNotify(Channel channel, String pattern, String owner);

  List<TvNotify> getTvNotifies(Channel channel) throws HokanDAOException;

  TvNotify getTvNotify(Channel channel, String pattern) throws HokanDAOException;
}
