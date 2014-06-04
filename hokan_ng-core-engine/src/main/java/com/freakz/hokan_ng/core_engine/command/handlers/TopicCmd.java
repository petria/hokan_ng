package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_TOPIC;

/**
 * Created by pairio on 4.6.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class TopicCmd extends Cmd {

  public TopicCmd() {
    super();
    setHelp("Sets current channels topic.");
    setChannelOnly(true);

    UnflaggedOption flg = new UnflaggedOption(ARG_TOPIC)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);


  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    Channel ch = request.getChannel();
    String topic = results.getString(ARG_TOPIC);
    if (topic == null) {
      response.addResponse("'%s' set by %s on %s", ch.getTopic(), ch.getTopicSetBy(), ch.getTopicSetDate());
    } else {
      String newTopic;
      if (topic.startsWith("+")) {
        newTopic = ch.getTopic() + " | " + topic.substring(1);
      } else {
        newTopic = topic;
      }
      response.addEngineMethodCall("setTopic", request.getChannel().getChannelName(), newTopic);
    }

  }
}
