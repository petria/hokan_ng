package com.freakz.hokan_ng.common.jms;

import com.freakz.hokan_ng.common.jms.messages.HokanMessageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Map;

/**
 * Created by petria on 12.12.2014.
 */
@Slf4j
public class HokanTopicListenerBase implements MessageListener {

  @Autowired
  private ApplicationContext context;

  @Override
  public void onMessage(Message message) {
    try {
//      log.info(this.getClass() + " -- Message: {}", message);
      if (message instanceof ObjectMessage) {
        ObjectMessage mapMessage = (ObjectMessage) message;
        HokanMessageObject messageObject = (HokanMessageObject) mapMessage.getObject();
        Map<String, HokanTopicFollower> followerMap = context.getBeansOfType(HokanTopicFollower.class);
        for (HokanTopicFollower follower : followerMap.values()) {
          follower.onTopicMessage(messageObject);
        }
      }
    } catch (Exception e) {
      log.error("Error: {}", e);
    }

  }
}
