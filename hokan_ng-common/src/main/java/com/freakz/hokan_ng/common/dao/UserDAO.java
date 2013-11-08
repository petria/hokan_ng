package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.User;

import java.util.List;

/**
 * User: petria
 * Date: 11/8/13
 * Time: 5:49 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UserDAO {

  User findUser(String nick);

  List<User> findUsers(int limit);

  List<User> getLoggedInUsers();

  void resetLoggedInUsers();

  void resetOlpos();

}
