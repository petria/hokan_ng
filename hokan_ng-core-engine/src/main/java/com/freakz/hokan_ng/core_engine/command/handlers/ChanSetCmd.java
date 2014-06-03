package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
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

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CHANNEL_ID;
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
    setHelp("Sets channel property. When executed via private messages valid channel id must be given.");
    addToHelpGroup(HelpGroup.PROPERTIES, this);
    setChannelOpOnly(true);

    UnflaggedOption flg = new UnflaggedOption(ARG_PROPERTY)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    flg = new UnflaggedOption(ARG_CHANNEL_ID)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String channelId = results.getString(ARG_CHANNEL_ID);
    Channel theChannel = request.getChannel();
    if (request.getIrcEvent().isPrivate()) {
      if (channelId == null) {
        response.addResponse("ChannelID parameter is needed when using private message. See ChanList command.");
        return;
      }
      long id = -1;
      try {
        id = Long.parseLong(channelId);
      } catch (NumberFormatException ex) {
        response.addResponse("Valid ChannelID parameter is needed!");
        return;
      }
      theChannel = channelService.findChannelById(id);
      if (theChannel == null) {
        response.addResponse("No valid Channel found with id: %d", id);
        return;
      }
    }

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
    ChannelProperty chanProp = properties.setChannelProperty(theChannel, propertyName, split[1]);
    response.addResponse("Property set: %s", chanProp.toString());
  }

}
