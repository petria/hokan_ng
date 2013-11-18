package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelUser;
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
public interface ChannelUsersDAO {

  ChannelUser createChannelUser(Channel channel, User user) throws HokanDAOException;

  void removeChannelUser(Channel channel, User user) throws HokanDAOException;

  void removeChannelUser(User user) throws HokanDAOException;

  List<ChannelUser> findChannelUsers(Channel channel) throws HokanDAOException;

  void clearChannelUsers(Channel channel) throws HokanDAOException;

  void clearChannelUsers() throws HokanDAOException;

}
