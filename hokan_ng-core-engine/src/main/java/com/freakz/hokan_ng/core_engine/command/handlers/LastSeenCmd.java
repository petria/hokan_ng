package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.service.UserChannelService;
import com.freakz.hokan_ng.common.service.UserService;
import com.freakz.hokan_ng.common.util.Uptime;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        String hokandev="hokan_dev";
        User hUser;

        if (nick == null) {
            hUser = request.getUser();
        } else {
            hUser = userService.findUser(nick);
            if (nick.equals(hokandev)) {
                response.addResponse("I'm here DUMBASS!");
                return;
            }
        }
            if (hUser == null) {
                    response.addResponse("User not found: %s", nick);
                    return;
                }

        List<UserChannel> userChannels = userChannelService.findUserChannels(hUser);
        if (userChannels.size() > 0) {
            UserChannel channel = userChannels.get(0);
            String ret = hUser.getNick() + " was last time seen on ";
            ret += channel.getChannel().getChannelName();
            String[] format = {"00", "00", "00", "0"};
            GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
            Date lastspoke = channel.getLastMessageTime();
            long temp = lastspoke.getTime();
            Uptime uptime = new Uptime(temp);
            Integer[] ut = uptime.getTimeDiff();
            String ret2 = StringStuff.fillTemplate(" and lastspoked %3 days and %2:%1:%0 ago", ut, format);
            ret += ret2;
            response.addResponse(ret);
        } else {
                response.addResponse("%s not seen on any known channel!", nick);
          }
    }
}