package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.util.Uptime;
import org.springframework.stereotype.Service;

/**
 * User: petria
 * Date: 11/6/13
 * Time: 7:13 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class UptimeServiceImpl implements UptimeService {


  private final Uptime uptime;

  public UptimeServiceImpl() {
    this.uptime = new Uptime();
  }

  @Override
  public Uptime getUptime() {
    return this.uptime;
  }
}
