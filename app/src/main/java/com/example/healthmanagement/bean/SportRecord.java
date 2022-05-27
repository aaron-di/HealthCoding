package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 运动记录
 */
public class SportRecord implements Serializable {
    private Integer id;//ID
    private Integer userId;//用户ID
    private Integer step;//步数
    private String remark;//备注
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

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public SportRecord(Integer id, Integer userId, Integer step, String remark, String date, String time) {
        this.id = id;
        this.userId = userId;
        this.step = step;
        this.remark = remark;
        this.date = date;
        this.time = time;
    }
}
