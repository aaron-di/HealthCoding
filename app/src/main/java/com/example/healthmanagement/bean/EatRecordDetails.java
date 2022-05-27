package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 饮食记录明细
 */
public class EatRecordDetails implements Serializable {
    private Integer id;//ID
    private Integer eatRecordId;//饮食记录
    private Integer foodEnergyId;//食物ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEatRecordId() {
        return eatRecordId;
    }

    public void setEatRecordId(Integer eatRecordId) {
        this.eatRecordId = eatRecordId;
    }

    public Integer getFoodEnergyId() {
        return foodEnergyId;
    }

    public void setFoodEnergyId(Integer foodEnergyId) {
        this.foodEnergyId = foodEnergyId;
    }
}
