package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.IrcEvent;
import com.freakz.hokan_ng.common.service.AccessControlService;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import com.freakz.hokan_ng.core_engine.dto.InternalRequest;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_EMAIL;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_FLAGS;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_FULL_NAME;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_JOIN_MSG;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_MASK;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_NICK;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PHONE;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_VERBOSE;

/**
 * User: petria
 * Date: 12/18/13
 * Time: 11:15 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class UserModCmd extends Cmd {

  @Autowired
  private AccessControlService accessControlService;
  @Autowired
  private UserChannelService userChannelService;
  @Autowired
  private UserService userService;

  public UserModCmd() {
    super();
    setHelp("Modify user information.");
    addToHelpGroup(HelpGroup.USERS, this);

    Switch sw = new Switch(ARG_VERBOSE)
        .setDefault("false")
        .setShortFlag('v');
    registerParameter(sw);

    FlaggedOption flg = new FlaggedOption(ARG_EMAIL)
        .setRequired(false)
        .setLongFlag("email")
        .setShortFlag('e');
    registerParameter(flg);

    flg = new FlaggedOption(ARG_FLAGS)
        .setRequired(false)
        .setLongFlag("flags")
        .setShortFlag('f');
    registerParameter(flg);

    flg = new FlaggedOption(ARG_FULL_NAME)
        .setRequired(false)
        .setLongFlag("fullname")
        .setShortFlag('n');
    registerParameter(flg);

    flg = new FlaggedOption(ARG_JOIN_MSG)
        .setRequired(false)
        .setLongFlag("joinmsg")
        .setShortFlag('j');
    registerParameter(flg);

    flg = new FlaggedOption(ARG_MASK)
        .setRequired(false)
        .setLongFlag("mask")
        .setShortFlag('m');
    registerParameter(flg);

    flg = new FlaggedOption(ARG_PHONE)
        .setRequired(false)
        .setLongFlag("phone")
        .setShortFlag('p');
    registerParameter(flg);

    UnflaggedOption opt = new UnflaggedOption(ARG_NICK)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    String mask = results.getString(ARG_MASK);
    String target = results.getString(ARG_NICK, "me");
    String email = results.getString(ARG_EMAIL);
    String flags = results.getString(ARG_FLAGS);
    String fullName = results.getString(ARG_FULL_NAME);
    String joinMsg = results.getString(ARG_JOIN_MSG);
    String phone = results.getString(ARG_PHONE);

    User hUser;
    IrcEvent iEvent = request.getIrcEvent();
    if (target.equals("me")) {
      hUser = ir.getUser();
    } else {
      if (accessControlService.isMasterUser(iEvent)) {
        hUser = userService.findUser(target);
      } else {
        response.addResponse("Only MasterUsers can modify others data!");
        return;
      }
    }

    if (hUser == null) {
      response.addResponse("No User found with: " + target);
      return;
    }
    UserChannel userChannel = userChannelService.getUserChannel(hUser, ir.getChannel());

    String ret = "";
    boolean updateUserChannel = false;
    if (email != null) {
      String old = hUser.getEmail();
      hUser.setEmail(email);
      ret += "Email    : '" + old + "' -> '" + email + "'\n";
    }
    if (flags != null) {
      String old = hUser.getFlags();
      hUser.setFlags(flags);
      ret += "Flags    : '" + old + "' -> '" + flags + "'\n";
    }
    if (fullName != null) {
      String old = hUser.getFullName();
      hUser.setFullName(fullName);
      ret += "Fullname : '" + old + "' -> '" + fullName + "'\n";
    }
    if (joinMsg != null) {
      String old = userChannel.getJoinComment();
      userChannel.setJoinComment(joinMsg);
      ret += "JoinMsg  : '" + old + "' -> '" + joinMsg + "'\n";
      updateUserChannel = true;
    }
    if (mask != null) {
      String old = hUser.getMask();
      hUser.setMask(mask);
      ret += "Mask     : '" + old + "' -> '" + mask + "'\n";
    }
    if (phone != null) {
      String old = hUser.getPhone();
      hUser.setPhone(phone);
      ret += "Phone  : '" + old + "' -> '" + phone + "'\n";
    }

    if (ret.length() > 0) {
      hUser = userService.updateUser(hUser);
      if (updateUserChannel) {
        userChannelService.storeUserChannel(userChannel);
      }
      if (results.getBoolean(ARG_VERBOSE)) {
        response.addResponse(hUser.getNick() + " datas modified: \n" + ret);
      } else {
        response.addResponse("Modified!");
      }
    } else {
      response.addResponse("Nothing modified!");
    }
  }

}
