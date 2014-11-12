package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.service.AccessControlService;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import com.freakz.hokan_ng.common.service.AccessControlService;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_NICK;


/**
 * Created by bzr on 10.11.2014.
 */
@Component
@Slf4j
@Scope("prototype")
public class LastSeenCmd extends Cmd  {

    @Autowired
    private AccessControlService accessControlService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserChannelService userChannelService;

    public LastSeenCmd() {
        super();
        setHelp("help");

        addToHelpGroup(HelpGroup.USERS, this);

        UnflaggedOption flg = new UnflaggedOption(ARG_NICK)
                .setRequired(false)
                .setGreedy(false);
        registerParameter(flg);
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
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

        String ret = hUser.getNick() + " was last time seen on ";
        List<UserChannel> userChannels = userChannelService.findUserChannels(hUser);
        UserChannel channel = userChannels.get(0);
        ret += channel.getChannel().getChannelName();
        ret += " - " + channel.getLastMessageTime() + "\n";

        response.addResponse(ret);

    }

}