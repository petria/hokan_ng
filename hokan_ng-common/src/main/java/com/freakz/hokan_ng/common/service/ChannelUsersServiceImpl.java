package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.ChannelUsersDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelUser;
import com.freakz.hokan_ng.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: petria
 * Date: 11/15/13
 * Time: 12:04 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class ChannelUsersServiceImpl implements ChannelUsersService {

  @Autowired
  private ChannelUsersDAO dao;

  public ChannelUsersServiceImpl() {
  }

  @Override
  public ChannelUser createChannelUser(Channel channel, User user) {
    return dao.createChannelUser(channel, user);
  }

  @Override
  public void removeChannelUser(Channel channel, User user) {
    dao.removeChannelUser(channel, user);
  }

  @Override
  public List<ChannelUser> findChannelUsers(Channel channel) {
    return dao.findChannelUsers(channel);
  }

  @Override
  public void clearChannelUsers(Channel channel) {
    dao.clearChannelUsers(channel);
  }
}
