package com.hzq.domain;

import com.google.gson.Gson;

public class Content {

  private String message;

  private Integer toId;

  private Integer fromId;

  private Integer groupId;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getToId() {
    return toId;
  }

  public void setToId(Integer toId) {
    this.toId = toId;
  }

  public Integer getFromId() {
    return fromId;
  }

  public void setFromId(Integer fromId) {
    this.fromId = fromId;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public String toJson(){
    return gson.toJson(this);
  }

  private static Gson gson = new Gson();

}