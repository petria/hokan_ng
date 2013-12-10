package com.freakz.hokan_ng.core_engine.rest;

import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanEngineException;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.service.PropertyService;
import com.freakz.hokan_ng.common.service.SystemTimer;
import com.freakz.hokan_ng.common.service.SystemTimerUser;
import com.freakz.hokan_ng.common.updaters.UpdaterManagerService;
import com.freakz.hokan_ng.core_engine.command.CommandHandlerService;
import com.freakz.hokan_ng.core_engine.command.handlers.Cmd;
import com.freakz.hokan_ng.core_engine.dto.InternalRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

/**
 * Date: 29.5.2013
 * Time: 09:31
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */

@Controller
@Slf4j
public class EngineController implements DisposableBean, SystemTimerUser {

  @Autowired
  private ApplicationContext context;

  @Autowired
  private CommandHandlerService commandHandler;

  @Autowired
  private PropertyService propertyService;

  @Autowired
  private SystemTimer systemTimer;

  @Autowired
  private UpdaterManagerService updaterManagerService;


  @RequestMapping(value = "/handle") //, produces = JSON, consumes = JSON)
  public
  @ResponseBody
  EngineResponse handleRequest(
      @RequestBody EngineRequest request
  ) {

    log.info("Got request: " + request);
    EngineResponse response = new EngineResponse(request);
    IrcMessageEvent ircMessageEvent = (IrcMessageEvent) request.getIrcEvent();
    Cmd handler = commandHandler.getCommandHandler(ircMessageEvent.getMessage());
    if (handler != null) {
      try {
        InternalRequest internalRequest = context.getBean(InternalRequest.class);
        internalRequest.init(request);
        handler.handleLine(internalRequest, response);
      } catch (Exception e) {
        HokanEngineException engineException = new HokanEngineException(e, handler.getClass().getName());
        response.setException(engineException.toString());
        log.error("Command handler returned exception {}", e);
      }
    }
    return response;
  }

  @PostConstruct
  public void postConstruct() throws HokanException {
    this.systemTimer.start();
    doSubscribe();
    this.updaterManagerService.start();
    propertyService.setProperty(PropertyName.PROP_SYS_CORE_ENGINE_UPTIME, "" + new Date().getTime());
  }

  @Override
  public void destroy() throws Exception {
    log.info("Destroying!");
    updaterManagerService.stop();
    this.systemTimer.stop();
    Thread.sleep(3 * 1000);
  }

  @Override
  public void doSubscribe() {
    systemTimer.addSystemTimerUser(this);
  }

  private int lastUpdated = -1;

  @Override
  public void timerTick(Calendar cal, int hh, int mm, int ss) throws Exception {
    if (mm != lastUpdated) {
      lastUpdated = mm;
      Property property = propertyService.findProperty(PropertyName.PROP_SYS_CORE_ENGINE_RUNTIME);
      if (property == null) {
        property = new Property(PropertyName.PROP_SYS_CORE_ENGINE_RUNTIME, "0", "");
      } else {
        if (property.getValue() == null) {
          property.setValue("0");
        } else {
          int value = Integer.parseInt(property.getValue());
          value += 60;
          property.setValue(value + "");
        }
      }
      propertyService.saveProperty(property);
    }
//    log.info("Timer tick!");
  }
}
