package com.freakz.hokan_ng.common.jms;

import java.io.Serializable;

/**
 * Created by petria on 11.12.2014.
 */
public interface TopicPublisher {

  void produce(final Serializable object);

}
