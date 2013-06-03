package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long companyId;

    @Column(name = "SERVER")
    private String server;

    @Column(name = "SERVER_PASSWORD")
    private String serverPassword;

    @Column(name = "SERVER_PORT")
    private int port;

    @Column(name = "USE_THROTTLE")
    private int useThrottle;

    @Column(name = "CHANNELS")
    private String channels;

    public IrcServer() {
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getUseThrottle() {
        return useThrottle;
    }

    public void setUseThrottle(int useThrottle) {
        this.useThrottle = useThrottle;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }
}
