package com.freakz.hokan_ng.core.dao;

import com.freakz.hokan_ng.common.entity.IrcServer;
import com.freakz.hokan_ng.core.exception.HokanException;

import java.util.List;

/**
 * Date: 3.6.2013
 * Time: 11:35
 *
 * @author Petri Airio (petri.airio@siili.fi)
 */
public interface IrcServerDAO {

    List<IrcServer> getIrcServers() throws HokanException;

}
