package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 健康档案
 */
public class HealthRecord implements Serializable {
    private Integer id;//ID
    private Integer userId;//用户ID
    private Float kcal;//大卡
    private Float weight;//体重
    private Float heartRate;//心率
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

    public Float getKcal() {
        return kcal;
    }

    public void setKcal(Float kcal) {
        this.kcal = kcal;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Float heartRate) {
        this.heartRate = heartRate;
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

    public HealthRecord(Integer id, Integer userId, Float kcal, Float weight, Float heartRate, String date, String time) {
        this.id = id;
        this.userId = userId;
        this.kcal = kcal;
        this.weight = weight;
        this.heartRate = heartRate;
        this.date = date;
        this.time = time;
    }
}
