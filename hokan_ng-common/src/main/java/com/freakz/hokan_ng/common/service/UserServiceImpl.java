package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.UserDAO;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanException;
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
  public List<User> findUsers() throws HokanException {
    return userDAO.findUsers();
  }

  @Override
  public List<User> getLoggedInUsers() throws HokanException {
    return userDAO.getLoggedInUsers();
  }

  @Override
  public void resetLoggedInUsers() throws HokanException {
    userDAO.resetLoggedInUsers();
  }

  @Override
  public void resetOlpos() throws HokanException {
    userDAO.resetOlpos();
  }

  @Override
  public User updateUser(User user) throws HokanException {
    return userDAO.updateUser(user);
  }
}
