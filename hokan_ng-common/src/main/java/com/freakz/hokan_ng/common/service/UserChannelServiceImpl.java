package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.UserChannelDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: petria
 * Date: 11/9/13
 * Time: 4:30 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class UserChannelServiceImpl implements UserChannelService {

  @Autowired
  private UserChannelDAO userChannelDAO;

  public UserChannelServiceImpl() {
  }

  @Override
  public UserChannel createUserChannel(User user, Channel channel) throws HokanException {
    return userChannelDAO.createUserChannel(user, channel);
  }

  @Override
  public UserChannel getUserChannel(User user, Channel channel) {
    try {
      return userChannelDAO.getUserChannel(user, channel);
    } catch (HokanException e) {
      //
    }
    return null;
  }

  @Override
  public List<UserChannel> findUserChannels(User user) throws HokanException {
    return userChannelDAO.findUserChannels(user);
  }

}
