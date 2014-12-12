package com.freakz.hokan_ng.common.jms;

import org.springframework.stereotype.Component;

/**
 * Created by petria on 12.12.2014.
 */
@Component
public class HokanJMSMessageHandler implements HokanTopicFollower {

  private HokanMessageObject reply;

  public synchronized HokanMessageObject createQuery(HokanTopicListener replyListener, HokanTopicPublisher querySender) {

    String jmsCorrelationID = "1234";
    HokanMessageObject query = new HokanMessageObject();
    query.setData("Test", "testing");
    replyListener.followTopic(this);
    querySender.publish(query, HokanTopicTypes.TO_SERVICE, jmsCorrelationID);
    while (reply == null) {
      try {
        wait();

      } catch (InterruptedException e) {
        // ignore
      }
    }
    replyListener.unFollowTopic(this);
    return this.reply;
  }

  @Override
  public String getAcceptedJMSType() {
    return ".*";
  }

  @Override
  public synchronized void onMessage(HokanMessageObject message) {
    this.reply = message;
    notifyAll();
  }
}
