package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 饮食记录
 */
public class EatRecordVo implements Serializable {
    private Integer id;//ID
    private Integer timeId;//时间段ID
    private Double kcal;//大卡
    private Double fat;//脂肪
    private Double protein;//蛋白质
    private Double carbon;//碳水
    private String date;//日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeId() {
        return timeId;
    }

    public void setTimeId(Integer timeId) {
        this.timeId = timeId;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarbon() {
        return carbon;
    }

    public void setCarbon(Double carbon) {
        this.carbon = carbon;
    }

    public EatRecordVo(Integer id, Integer timeId, Double kcal, Double fat, Double protein, Double carbon, String date) {
        this.id = id;
        this.timeId = timeId;
        this.kcal = kcal;
        this.fat = fat;
        this.protein = protein;
        this.carbon = carbon;
        this.date = date;
    }
}
