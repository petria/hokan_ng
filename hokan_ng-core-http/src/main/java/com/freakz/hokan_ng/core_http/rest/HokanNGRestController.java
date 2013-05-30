package com.freakz.hokan_ng.core_http.rest;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Date: 29.5.2013
 * Time: 09:31
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */

@Controller
public class HokanNGRestController {
    public static final String JSON = "application/json";

    @RequestMapping(value = "/hokan_ng/test")
    public
    @ResponseBody
    String testHandler() throws Exception {
        PircBot pircBot = new PircBot() {

        };
        pircBot.setAutoNickChange(true);
        pircBot.setVerbose(true);
        pircBot.connect("irc.tnnet.fi");
        pircBot.joinChannel("#HokanDev");

        return "foo";
    }

}
