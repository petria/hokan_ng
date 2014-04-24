package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.Properties;
import com.freakz.hokan_ng.common.util.JarScriptExecutor;
import com.freakz.hokan_ng.common.util.Uptime;
import com.martiansoftware.jsap.JSAPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 8:19 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class UptimeCmd extends Cmd {

  @Autowired
  private Properties properties;

  public UptimeCmd() {
    super();
    setHelp("Shows system and bot uptime.");
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
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    JarScriptExecutor cmdExecutor = new JarScriptExecutor("/uptime.sh", "UTF-8");
    String[] sysUptime = cmdExecutor.executeJarScript();
    long coreIoUptime = properties.getPropertyAsLong(PropertyName.PROP_SYS_CORE_IO_UPTIME, 0);
    long coreIoRuntime = properties.getPropertyAsLong(PropertyName.PROP_SYS_CORE_IO_RUNTIME, 0);
    long coreEngineUptime = properties.getPropertyAsLong(PropertyName.PROP_SYS_CORE_ENGINE_UPTIME, 0);
    long coreEngineRuntime = properties.getPropertyAsLong(PropertyName.PROP_SYS_CORE_ENGINE_RUNTIME, 0);
    Uptime ut1 = new Uptime(coreIoUptime);
    Uptime ut2 = new Uptime(coreEngineUptime);
    String uptime1 = String.format("System     :%s\n", sysUptime[0]);
    String uptime2 = String.format("Core-io    : %s (total runtime: %d sec)\n", ut1.toString(), coreIoRuntime);
    String uptime3 = String.format("Core-engine: %s (total runtime: %d sec)", ut2.toString(), coreEngineRuntime);
    response.addResponse(uptime1);
    response.addResponse(uptime2);
    response.addResponse(uptime3);
  }

}
