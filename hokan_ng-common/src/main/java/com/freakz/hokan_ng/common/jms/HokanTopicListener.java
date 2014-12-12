package com.freakz.hokan_ng.common.jms;

/**
 *
 * Created by petria on 11.12.2014.
 */
public interface HokanTopicListener {

  void followTopic(HokanTopicFollower follower);

  void unFollowTopic(HokanTopicFollower follower);

}
