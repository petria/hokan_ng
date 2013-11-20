package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import com.freakz.hokan_ng.common.rest.IrcEvent;

import java.util.List;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 8:51 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface AccessControlService {

  List<User> getMasterUsers() throws HokanException;

  List<User> getChannelOps(Channel channel) throws HokanServiceException;

  boolean isMasterUser(IrcEvent ircEvent);

  boolean isChannelOp(IrcEvent ircEvent);

}
