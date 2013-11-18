package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelUser;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanServiceException;

import java.util.List;

/**
 * User: petria
 * Date: 11/15/13
 * Time: 12:03 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface ChannelUsersService {

  ChannelUser createChannelUser(Channel channel, User user) throws HokanServiceException;

  void removeChannelUser(Channel channel, User user) throws HokanServiceException;

  List<ChannelUser> findChannelUsers(Channel channel) throws HokanServiceException;

  void clearChannelUsers(Channel channel) throws HokanServiceException;

  void clearChannelUsers() throws HokanServiceException;

}
