package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 8:52 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class AccessControlServiceServiceImpl implements AccessControlService {

  @Autowired
  private UserService userService;

  @Override
  public List<User> getMasterUsers() throws HokanServiceException {
    List<User> masterUsers = new ArrayList<>();
    masterUsers.add(userService.findUser("_Pete_"));
    return masterUsers;
  }

  @Override
  public List<User> getChannelOps(Channel channel) throws HokanServiceException {
    List<User> channelOps = new ArrayList<>();
    channelOps.add(userService.findUser("_Pete_"));
    return channelOps;
  }

}
