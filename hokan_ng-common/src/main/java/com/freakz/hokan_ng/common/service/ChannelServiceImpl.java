package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.ChannelDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 2:40 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
@Service
public class ChannelServiceImpl implements ChannelService {

  @Autowired
  private ChannelDAO channelDAO;

  @Override
  public List<Channel> findChannels(Network network, ChannelState state) {
    try {
      return channelDAO.findChannels(network, state);
    } catch (HokanException e) {
      log.error("Couldn't get Channel entity", e);
    }
    return null;
  }

  @Override
  public Channel findChannelByName(Network network, String name) {
    try {
      return channelDAO.findChannelByName(network, name);
    } catch (HokanException e) {
      log.error("Couldn't get Channel entity", e);
    }
    return null;
  }

  @Override
  public Channel createChannel(Network network, String name) {
    try {
      return channelDAO.createChannel(network, name);
    } catch (HokanException e) {
      log.error("Couldn't get Channel entity", e);
    }
    return null;
  }

  @Override
  public Channel updateChannel(Channel channel) {
    try {
      channel = channelDAO.updateChannel(channel);
    } catch (HokanException e) {
      log.error("Couldn't not update Channel entity", e);
    }
    return channel;
  }
}
