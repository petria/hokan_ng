package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.JoinedUser;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanDAOException;

import java.util.List;

/**
 * User: petria
 * Date: 11/13/13
 * Time: 2:43 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface JoinedUsersDAO {

  JoinedUser createJoinedUser(Channel channel, User user) throws HokanDAOException;

  JoinedUser getJoinedUser(Channel channel, User user) throws HokanDAOException;

  void removeJoinedUser(Channel channel, User user) throws HokanDAOException;

  void removeJoinedUser(User user) throws HokanDAOException;

  List<JoinedUser> findJoinedUsers(Channel channel) throws HokanDAOException;

  void clearJoinedUsers(Channel channel) throws HokanDAOException;

  void clearJoinedUsers() throws HokanDAOException;

}
