package com.freakz.hokan_ng.core_engine.dto;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.freakz.hokan_ng.common.service.ChannelService;
import com.freakz.hokan_ng.common.service.NetworkService;
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
public class InternalRequest extends EngineRequest implements Serializable {

  @Autowired
  private NetworkService networkService;

  @Autowired
  private ChannelService channelService;

  private EngineRequest request;
  private Network network;
  private Channel channel;

  public InternalRequest() {
  }

  public void init(EngineRequest request) throws HokanException {
    this.request = request;
    this.network = networkService.getNetwork(request.getIrcEvent().getNetwork());
    this.channel = channelService.findChannelByName(network, request.getIrcEvent().getChannel());
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
}
