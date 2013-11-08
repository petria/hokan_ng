package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;

import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 1:45 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface ChannelDAO {

  List<Channel> findChannels(Network network, ChannelState state) throws HokanException;

  Channel findChannelByName(Network network, String name) throws HokanException;

  Channel createChannel(Network network, String name) throws HokanException;

  Channel updateChannel(Channel channel) throws HokanException;
}
