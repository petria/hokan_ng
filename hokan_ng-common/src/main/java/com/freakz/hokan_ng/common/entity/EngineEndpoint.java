package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: petria
 * Date: 1/24/14
 * Time: 9:51 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Entity
@Table(name = "EngineEndpoint")
public class EngineEndpoint {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long id;

  @Column(name = "INSTANCE_KEY")
  private String instanceKey;

  @Column(name = "ENDPOINT_ADDRESS")
  private String endpointAddress;

  @Column(name = "LINE_MATCHER_REGEXP")
  private String lineMatcherRegexp;

  public EngineEndpoint() {
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

  public String getEndpointAddress() {
    return endpointAddress;
  }

  public void setEndpointAddress(String endpointAddress) {
    this.endpointAddress = endpointAddress;
  }

  public String getLineMatcherRegexp() {
    return lineMatcherRegexp;
  }

  public void setLineMatcherRegexp(String lineMatcherRegexp) {
    this.lineMatcherRegexp = lineMatcherRegexp;
  }
}
