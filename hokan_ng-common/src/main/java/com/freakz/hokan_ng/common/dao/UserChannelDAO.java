package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanDAOException;

import java.util.List;

/**
 * User: petria
 * Date: 11/9/13
 * Time: 4:04 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UserChannelDAO {

  UserChannel createUserChannel(User user, Channel channel) throws HokanDAOException;

  UserChannel getUserChannel(User user, Channel channel) throws HokanDAOException;

  List<UserChannel> findUserChannels(User user) throws HokanDAOException;

  UserChannel storeUserChannel(UserChannel userChannel);
}
