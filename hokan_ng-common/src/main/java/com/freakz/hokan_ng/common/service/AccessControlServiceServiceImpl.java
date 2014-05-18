package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.JoinedUser;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.freakz.hokan_ng.common.util.StringStuff;
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

  @Autowired
  private JoinedUsersService joinedUsersService;

  @Override
  public List<User> getMasterUsers() throws HokanServiceException {
    List<User> masterUsers = new ArrayList<>();
    masterUsers.add(userService.findUser("_Pete_"));
    masterUsers.add(userService.findUser("pairio"));
    masterUsers.add(userService.findUser("petria"));
    masterUsers.add(userService.findUser("petria_"));
    masterUsers.add(userService.findUser("cimistus"));
    return masterUsers;
  }

  @Override
  public List<User> getChannelOps(Channel channel) throws HokanServiceException {
    List<User> channelOps = new ArrayList<>();
    channelOps.add(userService.findUser("_Pete_"));
    channelOps.add(userService.findUser("pairio"));
    channelOps.add(userService.findUser("petria"));
    channelOps.add(userService.findUser("petria_"));
    channelOps.add(userService.findUser("cimistus"));
    return channelOps;
  }

  @Override
  public boolean isChannelOp(IrcEvent ircEvent, Channel ch) {
/*    if (ch == null) {
      return false;
    }
    try {
      for (User user : getChannelOps(ch)) {
        if (user != null && user.getNick().equalsIgnoreCase(ircEvent.getSender())) {
          return true;
        }
      }
    } catch (HokanServiceException e) {
      log.error("User error", e);
    }
    return false;*/
    return true;
  }

  @Override
  public boolean isMasterUser(IrcEvent ircEvent) {
    try {
      for (User user : getMasterUsers()) {
        if (user != null && user.getNick().equalsIgnoreCase(ircEvent.getSender())) {
          return true;
        }
      }
    } catch (HokanServiceException e) {
      log.error("User error", e);
    }
    return false;
  }

  @Override
  public User login(InternalRequest request, String password) {
    User user;
    try {
      user = userService.getUserByMask(request.getIrcEvent().getMask());
      if (user != null) {
        String userPassword = user.getPassword();
        if (userPassword.equals(StringStuff.md5(password))) {
          user.setLoggedIn(1);
          return userService.updateUser(user);
        }
      }
    } catch (HokanServiceException e) {
      log.error("User error", e);
    }
    return null;
  }

  @Override
  public boolean isUserLoggedIn(User user) {
    try {
      for (User loggedInUser : userService.getLoggedInUsers()) {
        if (user.getNick().matches(loggedInUser.getNick())) {
          return true;
        }
      }
    } catch (HokanServiceException e) {
      log.error("User error", e);
    }
    return false;
  }


  @Override
  public boolean isOp(Channel channel, User user) {
    JoinedUser joinedUser = joinedUsersService.getJoinedUser(channel, user);
    return joinedUser.isOp();
  }

}
