package com.freakz.hokan_ng.common.rest;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.exception.HokanServiceException;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.freakz.hokan_ng.common.service.NetworkService;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * User: petria
 * Date: 12/10/13
 * Time: 3:45 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
@Scope("prototype")
public class InternalRequest extends EngineRequest implements Serializable {

  @Autowired
  private NetworkService networkService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private UserChannelService userChannelService;

  @Autowired
  private UserService userService;


  private EngineRequest request;
  private Network network;
  private Channel channel;
  private User user;
  private UserChannel userChannel;

  public InternalRequest() {
  }

  public void init(EngineRequest request) throws HokanException {
    IrcMessageEvent ircMessageEvent = (IrcMessageEvent) request.getIrcEvent();

    this.request = request;
    this.network = networkService.getNetwork(request.getIrcEvent().getNetwork());
    this.user = getUser(request.getIrcEvent());
    if (!ircMessageEvent.isPrivate()) {
      this.channel = channelService.findChannelByName(network, request.getIrcEvent().getChannel());
      this.userChannel = userChannelService.getUserChannel(this.user, this.channel);
    }
  }

  public EngineRequest getRequest() {
    return this.request;
  }

  public IrcMessageEvent getIrcEvent() {
    return request.getIrcEvent();
  }

  public long getMyPid() {
    return request.getMyPid();
  }

  public Network getNetwork() {
    return network;
  }

  public Channel getChannel() {
    return channel;
  }

  public User getUser() {
    return user;
  }

  public void updateUser() {
    try {
      userService.updateUser(user);
    } catch (HokanServiceException e) {
      InternalRequest.log.error("User error", e);
    }
  }

  public UserChannel getUserChannel() {
    return userChannel;
  }

  public void updateUserChannel() {
    try {
      userChannelService.storeUserChannel(userChannel);
    } catch (HokanException e) {
      InternalRequest.log.error("UserChannel error", e);
    }
  }

  private User getUser(IrcEvent ircEvent) {
    try {
      User user;
      User maskUser = this.userService.getUserByMask(ircEvent.getMask());
      if (maskUser != null) {
        user = maskUser;
      } else {
        user = this.userService.findUser(ircEvent.getSender());
        if (user == null) {
          user = new User(ircEvent.getSender());
          user = userService.updateUser(user);
        }

      }
      return user;
    } catch (HokanException e) {
      InternalRequest.log.error("User error", e);
    }
    return null;
  }


}
