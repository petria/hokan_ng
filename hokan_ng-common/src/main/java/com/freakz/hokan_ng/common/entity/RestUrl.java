package com.freakz.hokan_ng.common.entity;

import javax.persistence.*;

/**
 * User: petria
 * Date: 1/24/14
 * Time: 9:51 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Entity
@Table(name = "RestUrl")
public class RestUrl {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long id;

  @Column(name = "INSTANCE_KEY")
  private String instanceKey;

  @Column(name = "REST_URL_TYPE")
  @Enumerated(EnumType.STRING)
  private RestUrlType restUrlType;

  @Column(name = "REST_URL")
  private String restUrl;

  @Column(name = "LINE_MATCHER_REGEXP")
  private String lineMatcherRegexp;

  public RestUrl() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getInstanceKey() {
    return instanceKey;
  }

  public void setInstanceKey(String instanceKey) {
    this.instanceKey = instanceKey;
  }

  public RestUrlType getRestUrlType() {
    return restUrlType;
  }

  public void setRestUrlType(RestUrlType restUrlType) {
    this.restUrlType = restUrlType;
  }

  public String getRestUrl() {
    return restUrl;
  }

  public void setRestUrl(String restUrl) {
    this.restUrl = restUrl;
  }

  public String getLineMatcherRegexp() {
    return lineMatcherRegexp;
  }

  public void setLineMatcherRegexp(String lineMatcherRegexp) {
    this.lineMatcherRegexp = lineMatcherRegexp;
  }

  public String toString() {
    return String.format("ID %2d: - InstanceKey: %10s - RestUrl: %s", id, instanceKey, restUrl);
  }

}
