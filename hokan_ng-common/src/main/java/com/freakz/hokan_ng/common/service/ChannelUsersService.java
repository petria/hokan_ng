package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelUser;
import com.freakz.hokan_ng.common.entity.User;

import java.util.List;

/**
 * User: petria
 * Date: 11/15/13
 * Time: 12:03 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface ChannelUsersService {

  ChannelUser createChannelUser(Channel channel, User user);

  void removeChannelUser(Channel channel, User user);

  List<ChannelUser> findChannelUsers(Channel channel);

  void clearChannelUsers(Channel channel);

}
