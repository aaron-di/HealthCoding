package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 体重速记
 */
public class WeightRecord implements Serializable {
    private Integer id;//ID
    private Integer userId;//用户ID
    private String weight;//重量
    private String date;//日期
    private String time;//时间

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public WeightRecord(Integer id, Integer userId, String weight, String date, String time) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.date = date;
        this.time = time;
    }
}
