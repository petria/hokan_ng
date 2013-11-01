package com.freakz.hokan_ng.core.service;

/**
 * Date: 3.6.2013
 * Time: 10:39
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */

public interface ConnectionManagerService {

	void goOnline(String network);

	void disconnect(String network);

	void disconnectAll();

}
