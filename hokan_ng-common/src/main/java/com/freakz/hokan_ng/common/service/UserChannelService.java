package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;

import java.util.List;

/**
 * User: petria
 * Date: 11/9/13
 * Time: 4:30 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UserChannelService {

  UserChannel createUserChannel(User user, Channel channel);

  UserChannel getUserChannel(User user, Channel channel);

  List<UserChannel> findUserChannels(User user) throws HokanException;

  UserChannel storeUserChannel(UserChannel userChannel) throws HokanException;

}
