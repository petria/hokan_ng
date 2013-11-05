package com.freakz.hokan_ng.core_http.rest;

import org.jibble.pircbot.PircBot;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date: 29.5.2013
 * Time: 09:31
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */

@Controller
public class HokanController {

	@RequestMapping(value = "/hokan_ng/goOnline")
	public
	@ResponseBody
	String goOnline() throws Exception {
    return "ok";
	}

}
