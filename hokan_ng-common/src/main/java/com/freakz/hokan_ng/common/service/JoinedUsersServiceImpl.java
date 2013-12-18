package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.JoinedUsersDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.JoinedUser;
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
public class JoinedUsersServiceImpl implements JoinedUsersService {

  @Autowired
  private JoinedUsersDAO dao;

  public JoinedUsersServiceImpl() {
  }

  @Override
  public JoinedUser createJoinedUser(Channel channel, User user) throws HokanServiceException {
    try {
      return dao.createJoinedUser(channel, user);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public JoinedUser getJoinedUser(Channel channel, User user) {
    try {
      return dao.getJoinedUser(channel, user);
    } catch (HokanDAOException e) {
      log.error("JoinedUser error", e);
    }
    return null;
  }

  @Override
  public void removeJoinedUser(Channel channel, User user) throws HokanServiceException {
    try {
      dao.removeJoinedUser(channel, user);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public List<JoinedUser> findJoinedUsers(Channel channel) throws HokanServiceException {
    try {
      return dao.findJoinedUsers(channel);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void clearJoinedUsers(Channel channel) throws HokanServiceException {
    try {
      dao.clearJoinedUsers(channel);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void clearJoinedUsers() throws HokanServiceException {
    try {
      dao.clearJoinedUsers();
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

}
