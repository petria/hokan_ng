package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.TvNotify;

import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 6:37 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface TvNotifyService {

  TvNotify addTvNotify(Channel channel, String pattern, String owner);

  List<TvNotify> getTvNotifies(Channel channel);

  TvNotify getTvNotify(Channel channel, String pattern);
}
