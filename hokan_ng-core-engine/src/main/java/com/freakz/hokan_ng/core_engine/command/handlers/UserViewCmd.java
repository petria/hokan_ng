package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.AccessControlService;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_NICK;

/**
 * User: petria
 * Date: 12/16/13
 * Time: 9:24 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
@Scope("prototype")
public class UserViewCmd extends Cmd {

  @Autowired
  private AccessControlService accessControlService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserChannelService userChannelService;

  public UserViewCmd() {
    super();
    setHelp("UserViewCmd help");
    addToHelpGroup(HelpGroup.USERS, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_NICK)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    String nick = results.getString(ARG_NICK);
    User hUser;
    if (nick == null) {
      hUser = ir.getUser();
    } else {
      hUser = userService.findUser(nick);
      if (hUser == null) {
        response.addResponse("User not found: %s", nick);
        return;
      }
    }

    String ret = "-= " + hUser.getNick() + " (" + hUser.getFullName();
    if (hUser.getEmail() != null && hUser.getEmail().length() > 0) {
      ret += ", " + hUser.getEmail();
    }
    ret += ") ";
    if (accessControlService.isMasterUser(request.getIrcEvent())) {
      ret += "[MasterUser] ";
    }
    if (accessControlService.isChannelOp(request.getIrcEvent(), ir.getChannel())) {
      ret += "[ChannelOp] ";
    }
    ret += "=-\n";

    ret += "SetMask  : " + hUser.getMask() + " (CurrentMask: " + hUser.getRealMask() + ")\n";
    ret += "Flags    : " + hUser.getFlags() + "\n";
    ret += "Channels :\n";

    List<UserChannel> userChannels = userChannelService.findUserChannels(hUser);
    for (UserChannel channel : userChannels) {
      ret += "  " + channel.getChannel().getChannelName();
      ret += " - " + channel.getLastMessageTime() + "\n";
    }

    response.addResponse(ret);
  }

}
