package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.JoinedUser;
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
public interface JoinedUsersService {

  JoinedUser createJoinedUser(Channel channel, User user) throws HokanServiceException;

  JoinedUser getJoinedUser(Channel channel, User user);

  void removeJoinedUser(Channel channel, User user) throws HokanServiceException;

  List<JoinedUser> findJoinedUsers(Channel channel) throws HokanServiceException;

  void clearJoinedUsers(Channel channel) throws HokanServiceException;

  void clearJoinedUsers() throws HokanServiceException;

}
