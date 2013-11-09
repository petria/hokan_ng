package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanException;

import java.util.List;

/**
 * User: petria
 * Date: 11/8/13
 * Time: 5:49 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UserDAO {

  User findUser(String nick) throws HokanException;

  List<User> findUsers() throws HokanException;

  List<User> getLoggedInUsers() throws HokanException;

  void resetLoggedInUsers() throws HokanException;

  void resetOlpos() throws HokanException;

  User updateUser(User user) throws HokanException;

}
