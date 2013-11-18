package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.ChannelUsersDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelUser;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class ChannelUsersServiceImpl implements ChannelUsersService {

  @Autowired
  private ChannelUsersDAO dao;

  public ChannelUsersServiceImpl() {
  }

  @Override
  public ChannelUser createChannelUser(Channel channel, User user) throws HokanServiceException {
    try {
      return dao.createChannelUser(channel, user);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void removeChannelUser(Channel channel, User user) throws HokanServiceException {
    try {
      dao.removeChannelUser(channel, user);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public List<ChannelUser> findChannelUsers(Channel channel) throws HokanServiceException {
    try {
      return dao.findChannelUsers(channel);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void clearChannelUsers(Channel channel) throws HokanServiceException {
    try {
      dao.clearChannelUsers(channel);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void clearChannelUsers() throws HokanServiceException {
    try {
      dao.clearChannelUsers();
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

}
