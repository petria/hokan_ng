package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelUser;
import com.freakz.hokan_ng.common.entity.User;

import java.util.List;

/**
 * User: petria
 * Date: 11/13/13
 * Time: 2:43 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface ChannelUsersDAO {

  ChannelUser createChannelUser(Channel channel, User user);

  void removeChannelUser(Channel channel, User user);

  void removeChannelUser(User user);

  List<ChannelUser> findChannelUsers(Channel channel);

  void clearChannelUsers(Channel channel);

}
