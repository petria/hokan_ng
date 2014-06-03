package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.Properties;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CHANNEL_ID;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 2:20 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class ChanEnvCmd extends Cmd {

  @Autowired
  private Properties properties;

  public ChanEnvCmd() {
    super();
    setHelp("Shows properties set for the channel");
    addToHelpGroup(HelpGroup.PROPERTIES, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_CHANNEL_ID)
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

    List propertyList = properties.getChannelProperties(theChannel);
    for (Object property : propertyList) {
      response.addResponse("%s\n", property.toString());
    }
  }
}
