package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.PropertyService;
import com.freakz.hokan_ng.common.util.TimeUtil;
import com.freakz.hokan_ng.common.util.Uptime;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 8:19 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class UptimeCmd extends Cmd {

  @Autowired
  private PropertyService propertyService;

  public UptimeCmd() {
    super();
  }

  @PostConstruct
  public void postInit() throws HokanException {
    propertyService.setProperty(PropertyName.PROP_SYS_CORE_ENGINE_UPTIME, "" + new Date().getTime());
  }

  @Override
  public String getMatchPattern() {
    return "!uptime.*";
  }

  @Override
  public String getName() {
    return "Uptime";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    Long coreHttp = Long.parseLong(propertyService.findProperty(PropertyName.PROP_SYS_CORE_HTTP_UPTIME).getValue());
    Long coreEngine = Long.parseLong(propertyService.findProperty(PropertyName.PROP_SYS_CORE_ENGINE_UPTIME).getValue());
    Uptime ut1 = new Uptime(coreHttp);
    Uptime ut2 = new Uptime(coreEngine);
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    Date time = TimeUtil.getDate();
    response.setResponseMessage(" " + sdf.format(time) + " core-io: " + ut1.toString() + ", core-engine: " + ut2.toString());
  }

}
