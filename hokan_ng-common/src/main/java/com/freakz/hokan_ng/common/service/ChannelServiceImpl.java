package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.ChannelDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 2:40 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class ChannelServiceImpl implements ChannelService {

  @Autowired
  private ChannelDAO channelDAO;

  @Override
  public List<Channel> findChannels(Network network, ChannelState state) throws HokanException {
    return channelDAO.findChannels(network, state);
  }

  @Override
  public Channel findChannelByName(Network network, String name) throws HokanException {
    return channelDAO.findChannelByName(network, name);
  }

  @Override
  @Transactional
  public Channel createChannel(Network network, String name) throws HokanException {
    return channelDAO.createChannel(network, name);
  }

}
