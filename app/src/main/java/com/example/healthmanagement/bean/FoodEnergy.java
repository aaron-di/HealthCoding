package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 菜谱
 */
public class FoodEnergy implements Serializable {
    private Integer id;//ID
    private Integer typeId;//类型
    private String food;//食品
    private String quantity;//数量
    private String heat;//热量kcal
    private Double fat;//脂肪
    private Double protein;//蛋白质
    private Double carbon;//碳水

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
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

    public FoodEnergy(Integer id, Integer typeId, String food, String quantity, String heat, Double fat, Double protein, Double carbon) {
        this.id = id;
        this.typeId = typeId;
        this.food = food;
        this.quantity = quantity;
        this.heat = heat;
        this.fat = fat;
        this.protein = protein;
        this.carbon = carbon;
    }

}
