
package com.example.healthmanagement.bean;


public class BMIResult {

    private double idealWeight;
    private String normalWeight;
    private int level;
    private String levelMsg;
    private String danger;
    private double bmi;
    private String normalBMI;
    public void setIdealWeight(double idealWeight) {
         this.idealWeight = idealWeight;
     }
     public double getIdealWeight() {
         return idealWeight;
     }

    public void setNormalWeight(String normalWeight) {
         this.normalWeight = normalWeight;
     }
     public String getNormalWeight() {
         return normalWeight;
     }

    public void setLevel(int level) {
         this.level = level;
     }
     public int getLevel() {
         return level;
     }

    public void setLevelMsg(String levelMsg) {
         this.levelMsg = levelMsg;
     }
     public String getLevelMsg() {
         return levelMsg;
     }

    public void setDanger(String danger) {
         this.danger = danger;
     }
     public String getDanger() {
         return danger;
     }

    public void setBmi(double bmi) {
         this.bmi = bmi;
     }
     public double getBmi() {
         return bmi;
     }

    public void setNormalBMI(String normalBMI) {
         this.normalBMI = normalBMI;
     }
     public String getNormalBMI() {
         return normalBMI;
     }

}