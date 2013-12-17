package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanServiceException;

import java.util.List;

/**
 * User: petria
 * Date: 11/9/13
 * Time: 3:42 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UserService {

  User findUser(String nick) throws HokanServiceException;

  List<User> findUsers() throws HokanServiceException;

  User getUserByMask(String mask) throws HokanServiceException;

  List<User> getLoggedInUsers() throws HokanServiceException;

  void resetLoggedInUsers() throws HokanServiceException;

  void resetOlpos() throws HokanServiceException;

  User updateUser(User user) throws HokanServiceException;

}
