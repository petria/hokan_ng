package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.JoinedUser;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_TARGET;

/**
 * User: petria
 * Date: 12/19/13
 * Time: 7:25 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class OpCmd extends Cmd {

  public OpCmd() {
    super();
    setHelp("Gives operator rights to target user on channel.");
    addToHelpGroup(HelpGroup.CHANNELS, this);
    addToHelpGroup(HelpGroup.USERS, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_TARGET)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);

    setLoggedInOnly(false);
    setChannelOpOnly(false);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String target = results.getString(ARG_TARGET);
    if (request.getIrcEvent().isPrivate()) {
      if (target == null) {
        target = request.getIrcEvent().getSender();
      }
      List<JoinedUser> joinedUsers = joinedUsersService.findJoinedUsers(request.getNetwork());
      boolean didOp = false;
      for (JoinedUser joinedUser : joinedUsers) {
        if (joinedUser.getUser().getNick().toLowerCase().matches(".*" + target.toLowerCase() + ".*")) {
          response.addEngineMethodCall("op", joinedUser.getChannel().getChannelName(), joinedUser.getUser().getNick());
          response.addResponse("Trying to op %s on channel %s\n", joinedUser.getUser().getNick(), joinedUser.getChannel().getChannelName());
          didOp = true;
        }
      }
      if (didOp == false) {
        response.addResponse("Didn't find anyone matching %s !", target);
      }

    } else {

      if (!request.getIrcEvent().isBotOp()) {
        response.addResponse("I need OPs!");
        return;
      }

      if (target == null) {
        response.addEngineMethodCall("op", request.getChannel().getChannelName(), request.getIrcEvent().getSender());
      } else {
        response.addEngineMethodCall("op", request.getChannel().getChannelName(), target);
      }
    }
  }

}
