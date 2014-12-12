package com.freakz.hokan_ng.common.jms;

import lombok.extern.slf4j.Slf4j;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petria on 12.12.2014.
 */
@Slf4j
public class HokanTopicListenerImpl implements MessageListener, HokanTopicListener {

  private List<HokanTopicFollower> followers = new ArrayList<>();


  public void followTopic(HokanTopicFollower follower) {
    followers.add(follower);
  }

  public void unFollowTopic(HokanTopicFollower follower) {
    followers.remove(follower);
  }

  @Override
  public void onMessage(Message message) {
    try {
      if (message instanceof ObjectMessage) {
        if (message.getJMSType().equals(HokanTopicTypes.TO_IO)) {
          ObjectMessage mapMessage = (ObjectMessage) message;
          HokanMessageObject messageObject = (HokanMessageObject) mapMessage.getObject();
          for (HokanTopicFollower follower : this.followers) {
            follower.onMessage(messageObject);
          }
        }
      }
    } catch (Exception e) {
      log.error("Error: {}", e);
    }

  }
}
