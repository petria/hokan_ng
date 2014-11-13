package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
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
 *
 * Created by bzr on 10.11.2014.
 */
@Component
@Slf4j
@Scope("prototype")
public class LastSeenCmd extends Cmd  {

    @Autowired
    private UserService userService;

    @Autowired
    private UserChannelService userChannelService;

    public LastSeenCmd() {
        super();
        setHelp("Shows when user has last seen.");

        addToHelpGroup(HelpGroup.USERS, this);

        UnflaggedOption flg = new UnflaggedOption(ARG_NICK)
                .setRequired(false)
                .setGreedy(false);
        registerParameter(flg);
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

        String nick = results.getString(ARG_NICK);
        User hUser;
        if (nick == null) {
            hUser = request.getUser();
        } else {
            hUser = userService.findUser(nick);
            if (hUser == null) {
                response.addResponse("User not found: %s", nick);
                return;
            }
        }

        List<UserChannel> userChannels = userChannelService.findUserChannels(hUser);
        if (userChannels.size() > 0) {
            UserChannel channel = userChannels.get(0);
            String ret = hUser.getNick() + " was last time seen on ";
            ret += channel.getChannel().getChannelName();
            ret += " - " + channel.getLastMessageTime() + "\n";

            response.addResponse(ret);
        } else {
            response.addResponse("%s not seen on any known channel!", nick);
        }

    }

}