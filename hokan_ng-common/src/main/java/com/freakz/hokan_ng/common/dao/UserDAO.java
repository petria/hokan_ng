package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanDAOException;

import java.util.List;

/**
 * User: petria
 * Date: 11/8/13
 * Time: 5:49 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UserDAO {

  User findUser(String nick) throws HokanDAOException;

  List<User> findUsers() throws HokanDAOException;

  List<User> getLoggedInUsers() throws HokanDAOException;

  void resetLoggedInUsers() throws HokanDAOException;

  void resetOlpos() throws HokanDAOException;

  User updateUser(User user) throws HokanDAOException;

}
