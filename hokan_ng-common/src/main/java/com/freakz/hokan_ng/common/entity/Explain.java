package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * Created by AirioP on 21.8.2014.
 */
@Entity
@Table(name = "Explain")
public class Explain extends HokanBaseEntity implements Serializable {

  @Column(name = "EXPLAIN_KEY")
  private String explainKey;

  @Column(name = "EXPLAIN_VALUE")
  private String explainValue;

  public Explain() {
    super();
    this.explainKey = "";
    this.explainValue = "";
  }

  public Explain(String explainKey, String explainValue) {
    super();
    this.explainKey = explainKey;
    this.explainValue = explainValue;
  }

  public String getExplainKey() {
    return explainKey;
  }

  public void setExplainKey(String explainKey) {
    this.explainKey = explainKey;
  }

  public String getExplainValue() {
    return explainValue;
  }

  public void setExplainValue(String explainValue) {
    this.explainValue = explainValue;
  }
}
