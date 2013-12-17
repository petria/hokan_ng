package com.freakz.hokan_ng.core_engine.dto;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.freakz.hokan_ng.common.service.NetworkService;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InternalRequest extends EngineRequest implements Serializable {

  @Autowired
  private NetworkService networkService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private UserService userService;

  private EngineRequest request;
  private Network network;
  private Channel channel;
  private User user;

  public InternalRequest() {
  }

  public void init(EngineRequest request) throws HokanException {
    this.request = request;
    this.network = networkService.getNetwork(request.getIrcEvent().getNetwork());
    this.channel = channelService.findChannelByName(network, request.getIrcEvent().getChannel());
    this.user = getUser(request.getIrcEvent());
  }

  public EngineRequest getRequest() {
    return this.request;
  }

  public IrcEvent getIrcEvent() {
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
      log.error("User error", e);
    }
    return null;
  }

}
