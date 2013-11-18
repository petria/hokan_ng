package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.UserDAO;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: petria
 * Date: 11/9/13
 * Time: 3:42 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDAO userDAO;

  public UserServiceImpl() {
  }

  @Override
  public User findUser(String nick) {
    try {
      return userDAO.findUser(nick);
    } catch (HokanException e) {
      //
    }
    return null;
  }

  @Override
  public List<User> findUsers() throws HokanServiceException {
    try {
      return userDAO.findUsers();
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public List<User> getLoggedInUsers() throws HokanServiceException {
    try {
      return userDAO.getLoggedInUsers();
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void resetLoggedInUsers() throws HokanServiceException {
    try {
      userDAO.resetLoggedInUsers();
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public void resetOlpos() throws HokanServiceException {
    try {
      userDAO.resetOlpos();
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

  @Override
  public User updateUser(User user) throws HokanServiceException {
    try {
      return userDAO.updateUser(user);
    } catch (HokanDAOException e) {
      throw new HokanServiceException(e);
    }
  }

}
