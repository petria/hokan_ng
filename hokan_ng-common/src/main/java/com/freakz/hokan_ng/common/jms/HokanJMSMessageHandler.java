package com.freakz.hokan_ng.common.jms;

import com.freakz.hokan_ng.common.jms.messages.HokanMessageObject;
import com.freakz.hokan_ng.common.jms.messages.HokanServiceReply;
import com.freakz.hokan_ng.common.jms.messages.HokanServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * Created by petria on 12.12.2014.
 */
@Component
@Slf4j
public class HokanJMSMessageHandler implements HokanTopicFollower {

  private HokanMessageObject reply;

  public synchronized HokanMessageObject createQuery(HokanTopicFollower replyListener, HokanTopicPublisher querySender) {

    String jmsCorrelationID = "1234";
    HokanServiceRequest query = new HokanServiceRequest();
    query.setData("Test", "testing");
    querySender.publish(query, "", jmsCorrelationID);
    int waitCount = 10;
    while (reply == null) {
      try {
        wait(512);

      } catch (InterruptedException e) {
        // ignore
      }
      log.info("Waiting reply: {}", waitCount);
      waitCount--;
      if (waitCount == 0) {
        break;
      }
    }
    return this.reply;
  }

  @Override
  public String getAcceptedJMSType() {
    return ".*";
  }

  @Override
  public synchronized void onTopicMessage(HokanMessageObject message) {
    if (message instanceof HokanServiceReply) {
      log.info("Got reply: {}", message);
      this.reply = message;
      notifyAll();
    }
  }
}
