package com.freakz.hokan_ng.core_engine.jms;

import com.freakz.hokan_ng.common.exception.HokanEngineException;
import com.freakz.hokan_ng.common.jms.HokanMessageObject;
import com.freakz.hokan_ng.common.jms.HokanTopicFollower;
import com.freakz.hokan_ng.common.jms.HokanTopicListenerImpl;
import com.freakz.hokan_ng.common.jms.HokanTopicTypes;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.rest.messages.EngineRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.core_engine.command.CommandHandlerService;
import com.freakz.hokan_ng.core_engine.command.handlers.Cmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreEngineTopicListener extends HokanTopicListenerImpl implements HokanTopicFollower {

  @Autowired
  private ApplicationContext context;

  @Autowired
  private CommandHandlerService commandHandler;

  @Autowired
  private CoreEngineTopicPublisher topicPublisher;

  public CoreEngineTopicListener() {
    followTopic(this);
  }

  private void handleEngineRequest(EngineRequest request) {
    log.info("Got request: " + request);
    EngineResponse response = new EngineResponse(request);
    IrcMessageEvent ircMessageEvent = request.getIrcEvent();
    Cmd handler = commandHandler.getCommandHandler(ircMessageEvent.getMessage());
    log.info("message: " + ircMessageEvent.getMessage());
    if (handler != null) {
      InternalRequest internalRequest;
      try {
        internalRequest = context.getBean(InternalRequest.class);
        internalRequest.init(request);
        if (!ircMessageEvent.isPrivate()) {
          internalRequest.getUserChannel().setLastCommand(handler.getName());
          internalRequest.getUserChannel().setLastCommandTime(new Date());
          internalRequest.updateUserChannel();
        }
        handler.handleLine(internalRequest, response);
      } catch (Exception e) {
        HokanEngineException engineException = new HokanEngineException(e, handler.getClass().getName());
        response.setException(engineException.toString());
        log.error("Command handler returned exception {}", e);
      }
      HokanMessageObject messageObject = new HokanMessageObject();
      messageObject.setData("engineResponse", response);
      topicPublisher.publish(messageObject, HokanTopicTypes.TO_IO, "1234");
    }
  }

  @Override
  public String getAcceptedJMSType() {
    return HokanTopicTypes.TO_ENGINE;
  }

  @Override
  public void onMessage(HokanMessageObject message) {
    log.info("message!!!");
    EngineRequest request = (EngineRequest) message.getData("engineRequest");
    if (request != null) {
      handleEngineRequest(request);
    }
  }
}
