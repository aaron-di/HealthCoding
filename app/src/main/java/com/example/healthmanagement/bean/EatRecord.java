package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 饮食记录
 */
public class EatRecord implements Serializable {
    private Integer id;//ID
    private Integer userId;//用户ID
    private Integer timeId;//时间段ID
    private String date;//日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTimeId() {
        return timeId;
    }

    public void setTimeId(Integer timeId) {
        this.timeId = timeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
