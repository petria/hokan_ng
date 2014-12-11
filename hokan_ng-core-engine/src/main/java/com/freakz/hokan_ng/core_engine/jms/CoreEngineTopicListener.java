package com.freakz.hokan_ng.core_engine.jms;

import com.freakz.hokan_ng.common.exception.HokanEngineException;
import com.freakz.hokan_ng.common.jms.HokanTopicMessageObject;
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

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Date;

/**
 *
 * Created by petria on 10.12.2014.
 */
@Slf4j
public class CoreEngineTopicListener implements MessageListener {

  @Autowired
  private ApplicationContext context;

  @Autowired
  private CommandHandlerService commandHandler;

  @Autowired
  private CoreEngineTopicPublisher topicPublisher;

  @Override
  public void onMessage(Message message) {
    log.info("Message: {}", message);
    try {
      if (message instanceof ObjectMessage) {
        if (message.getJMSType().equals(HokanTopicTypes.TO_ENGINE)) {
          ObjectMessage mapMessage = (ObjectMessage) message;
          HokanTopicMessageObject messageObject = (HokanTopicMessageObject) mapMessage.getObject();
          handleEngineRequest((EngineRequest) messageObject.getData("engineRequest"));
        }
      }
    } catch (Exception e) {
      log.error("Error: {}", e);
    }
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
      HokanTopicMessageObject messageObject = new HokanTopicMessageObject();
      messageObject.setData("engineResponse", response);
      topicPublisher.publish(messageObject, HokanTopicTypes.TO_IO);
    }
  }
}
