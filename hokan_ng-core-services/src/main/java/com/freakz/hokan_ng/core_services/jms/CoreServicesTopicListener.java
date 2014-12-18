package com.freakz.hokan_ng.core_services.jms;

import com.freakz.hokan_ng.common.jms.HokanTopicFollower;
import com.freakz.hokan_ng.common.jms.HokanTopicListenerBase;
import com.freakz.hokan_ng.common.jms.HokanTopicTypes;
import com.freakz.hokan_ng.common.jms.messages.HokanMessageObject;
import com.freakz.hokan_ng.common.jms.messages.HokanServiceReply;
import com.freakz.hokan_ng.common.jms.messages.HokanServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreServicesTopicListener extends HokanTopicListenerBase implements HokanTopicFollower {

  @Autowired
  private CoreServicesTopicPublisher topicPublisher;


  @Override
  public String getAcceptedJMSType() {
    return HokanTopicTypes.TO_SERVICE;
  }

  @Override
  public void onTopicMessage(HokanMessageObject message) {
    if (message instanceof HokanServiceRequest) {
      log.info("message!: {}", message);
      HokanServiceReply reply = new HokanServiceReply();
      reply.setData("hokanServiceReply", "serviceReply");
      topicPublisher.publish(reply, HokanTopicTypes.TO_ENGINE, "1234");
    }
  }
}
