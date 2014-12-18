package com.freakz.hokan_ng.common.jms;

import com.freakz.hokan_ng.common.jms.messages.HokanMessageObject;

/**
 * Created by petria on 12.12.2014.
 */
public interface HokanTopicFollower {

  String getAcceptedJMSType();

  void onTopicMessage(HokanMessageObject message);

}
