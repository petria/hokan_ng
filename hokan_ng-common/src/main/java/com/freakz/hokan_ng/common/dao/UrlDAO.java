package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Url;

import java.util.Date;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 10:11 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UrlDAO {

  Url findUrl(String url);

  Url storeUrl(Url entity);

  Url createUrl(String url, String sender, String channel, Date date);
}
