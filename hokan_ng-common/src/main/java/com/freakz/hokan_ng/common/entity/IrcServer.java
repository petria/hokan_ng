package com.freakz.hokan_ng.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Date: 3.6.2013
 * Time: 10:39
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Entity
@Table(name = "IrcServer")
public class IrcServer {

    private String server;
    private String serverPassword;
    private int port;
    private int useThrottle;

    private String channels;


}
