package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.UserService;
import com.freakz.hokan_ng.common.util.StringStuff;
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
 * Date: 12/31/13
 * Time: 10:00 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
@Scope("prototype")
public class UsersCmd extends Cmd {

  @Autowired
  private UserService userService;

  public UsersCmd() {
    super();
    setHelp("Lists users the Bot has meet on channels.");
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
    if (nick == null) {
      nick = ".*";
    } else {
      nick = ".*" + nick + ".*";
    }
    List<User> users = userService.findUsers();
    response.addResponse("Known users: ");
    for (User user : users) {
      if (StringStuff.match(user.getNick(), nick)) {
        response.addResponse("%s ", user.getNick());
      }
    }
  }

}
