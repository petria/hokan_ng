package com.freakz.hokan_ng.common.jms;

/**
 *
 * Created by petria on 12.12.2014.
 */
public interface HokanTopicFollower {

  String getAcceptedJMSType();

  void onMessage(HokanMessageObject message);

}
