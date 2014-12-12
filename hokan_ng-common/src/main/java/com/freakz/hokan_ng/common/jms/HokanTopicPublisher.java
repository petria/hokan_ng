package com.freakz.hokan_ng.common.jms;

import java.io.Serializable;

/**
 * Created by petria on 11.12.2014.
 */
public interface HokanTopicPublisher {

  void publish(final Serializable object, final String jmsType, final String jmsCorrelationID);

}
