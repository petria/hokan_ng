package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.ChannelProperty;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.Properties;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PROPERTY;

/**
 * User: petria
 * Date: 12/10/13
 * Time: 3:02 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class ChanSetCmd extends Cmd {

  @Autowired
  private Properties properties;

  public ChanSetCmd() {
    super();
    setHelp("Sets channel property.");
    addToHelpGroup(HelpGroup.PROPERTIES, this);
    setChannelOpOnly(true);

    UnflaggedOption flg = new UnflaggedOption(ARG_PROPERTY)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String[] split = results.getString(ARG_PROPERTY).split("=");
    if (split.length != 2) {
      response.addResponse("Syntax error, usage: %s <PropertyName>=<Value>", getName());
      return;
    }

    PropertyName propertyName = properties.getPropertyName(split[0]);
    if (propertyName == null) {
      response.addResponse("Invalid property: %s", split[0]);
      return;
    }
    ChannelProperty chanProp = new ChannelProperty(request.getChannel(), propertyName, split[1], "");
    chanProp = properties.saveChannelProperty(chanProp);
    response.addResponse("Property set: %s", chanProp.toString());
  }
}
